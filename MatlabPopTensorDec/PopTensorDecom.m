% require the Toolbox of tensor
% downloaded from http://www.sandia.gov/~tgkolda/TensorToolbox/

clear;
clc;

% read two control tables and one sample seed:
Ind_GenProvResTypeAge=import_data('Ind_GenProvResTypeAge.txt');
Ind_GenProvResTypeHHType=import_data('Ind_GenProvResTypeHHType.txt');
Ind_Sample=import_data('Ind_Sample.txt');

% objective joint distribution: gender*resideProv*resideType*ageInter*hhType

opts.maxit = 1000; opts.tol = 1e-4;% parameters
% sample tensor non-negatitve decomosition:
[factorMatrix,sample_core,out] = ntd(Ind_Sample,[2,31,3,21,2],opts);

main_core = sample_core;% set tensor core as the start point

% initialize two constraints:
GenProvResTypeAge = zeros(2,31,3,21);
for dim_1=1:2
	for dim_2=1:31
		for dim_3=1:3
			for dim_4=1:21
				GenProvResTypeAge(dim_1,dim_2,dim_3,dim_4) = Ind_GenProvResTypeAge(dim_1,dim_2,dim_3,dim_4);
			end
		end
	end
end
ConstraintCore_1 = ComputeCoreCons_1(GenProvResTypeAge,factorMatrix{1,1},factorMatrix{1,2},factorMatrix{1,3},factorMatrix{1,4});

GenProvResTypeHHType = zeros(2,31,3,2);
for dim_1=1:2
	for dim_2=1:31
		for dim_3=1:3
			for dim_5=1:2
				GenProvResTypeHHType(dim_1,dim_2,dim_3,dim_5) = Ind_GenProvResTypeHHType(dim_1,dim_2,dim_3,dim_5);
			end
		end
	end
end
ConstraintCore_2 = ComputeCoreCons_2(GenProvResTypeHHType,factorMatrix{1,1},factorMatrix{1,2},factorMatrix{1,3},factorMatrix{1,5});

tic;
% gradient decent
gamma = 0.01;
last_error = 10^10;
for iter=1:100
	% update
	temp_core = main_core;
	for dim_1=1:2
		for dim_2=1:31
			for dim_3=1:3
				for dim_4=1:21
					for dim_5=1:2
						summary = 0;
						for row=1:2
							for col=1:2
								summary = summary + factorMatrix{1,5}(row,col) * temp_core(dim_1,dim_2,dim_3,dim_4,col);
							end
						end
						summary = summary-ConstraintCore_1(dim_1,dim_2,dim_3,dim_4);
						coeff = sum(factorMatrix{1,5}(:,dim_5));
						gradient_1 = summary*coeff;

						summary = 0;
						for row=1:21
							for col=1:21
								summary = summary + factorMatrix{1,4}(row,col) * temp_core(dim_1,dim_2,dim_3,col,dim_5) ;
							end
						end
						summary = summary-ConstraintCore_2(dim_1,dim_2,dim_3,dim_5);
						coeff = sum(factorMatrix{1,4}(:,dim_4));
						gradient_2 = summary*coeff;
						main_core(dim_1,dim_2,dim_3,dim_4,dim_5) = temp_core(dim_1,dim_2,dim_3,dim_4,dim_5) - gamma*(gradient_1+gradient_2);
					end
				end
			end
		end
	end
	% compute error
	error_1 = 0;
	for dim_1=1:2
		for dim_2=1:31
			for dim_3=1:3
				for dim_4=1:21
					summary = 0;
					for row=1:2
						for col=1:2
							summary = summary + factorMatrix{1,5}(row,col) * main_core(dim_1,dim_2,dim_3,dim_4,col);
						end
					end
					error_1 = error_1 + 0.5*((summary - ConstraintCore_1(dim_1,dim_2,dim_3,dim_4))^2);
				end
			end
		end
	end
	error_2 = 0;
	for dim_1=1:2
		for dim_2=1:31
			for dim_3=1:3
				for dim_5=1:2
					summary = 0;
					for row=1:21
						for col=1:21
							summary = summary + factorMatrix{1,4}(row,col) * main_core(dim_1,dim_2,dim_3,col,dim_5);
						end
					end
						error_2 = error_2 + 0.5*((summary - ConstraintCore_2(dim_1,dim_2,dim_3,dim_5))^2);
				end
			end
		end
	end
	relativeError = error_1 + error_2;
	if relativeError < 50 % set convergence threshold to be 50
		fprintf('final %d: relativeError = ',relativeError);
		break;
	end
	last_error = relativeError;
	fprintf('%d: error_1 = %.3f,  error_2 = %.3f, total error =%.3f \n',iter,error_1,error_2,relativeError);
end
disp(['run time: ',num2str(toc)]);

% use the computed core and factor matrice to recover the population
% frequency tensor
recon = Recovery_full(main_core,factorMatrix{1,1},factorMatrix{1,2},factorMatrix{1,3},factorMatrix{1,4},factorMatrix{1,5});

% save to file
fid = fopen('PopJointDisTensor.txt', 'a+');
for dim_5=1:2
	for dim_4=1:21
		for dim_3=1:3
			for dim_2=1:31
				for dim_1=1:2
					fprintf(fid,'%.0f\n',recon(dim_1,dim_2,dim_3,dim_4,dim_5));
				end
			end
		end
	end
end
fclose(fid);