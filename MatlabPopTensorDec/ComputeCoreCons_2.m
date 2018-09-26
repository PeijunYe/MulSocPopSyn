function core = ComputeCoreCons_2(origTensor,U_1,U_2,U_3,U_4)

% this function computes the second constraint to the core
% the result covers dimension 1, 2, 3, and 5, that is:
% gender * resideProv * resideType * hhType

MP_pseudo_inverse_1 = inv(U_1);
Constraint_1 = zeros(2,31,3,2);
for dim_2=1:31
	for dim_3=1:3
		for dim_4=1:2
			for i=1:2
				sum_product = 0;
				for j=1:2
					sum_product = sum_product + MP_pseudo_inverse_1(i,j) * origTensor(j,dim_2,dim_3,dim_4);
				end
				Constraint_1(i,dim_2,dim_3,dim_4) = sum_product;
			end
		end
	end
end
MP_pseudo_inverse_2 = inv(U_2);
Constraint_2 = zeros(2,31,3,2);
for dim_1=1:2
	for dim_3=1:3
		for dim_4=1:2
			for i=1:31
				sum_product = 0;
				for j=1:31
					sum_product = sum_product + MP_pseudo_inverse_2(i,j) * Constraint_1(dim_1,j,dim_3,dim_4);
				end
				Constraint_2(dim_1,i,dim_3,dim_4) = sum_product;
			end
		end
	end
end
MP_pseudo_inverse_3 = inv(U_3);
Constraint_3 = zeros(2,31,3,2);
for dim_1=1:2
	for dim_2=1:31
		for dim_4=1:2
			for i=1:3
				sum_product = 0;
				for j=1:3
					sum_product = sum_product + MP_pseudo_inverse_3(i,j) * Constraint_2(dim_1,dim_2,j,dim_4);
				end
				Constraint_3(dim_1,dim_2,i,dim_4) = sum_product;
			end
		end
	end
end
MP_pseudo_inverse_4 = inv(U_4);
Constraint_4 = zeros(2,31,3,2);
for dim_1=1:2
	for dim_2=1:31
		for dim_3=1:3
			for i=1:2
				sum_product = 0;
				for j=1:2
					sum_product = sum_product + MP_pseudo_inverse_4(i,j) * Constraint_3(dim_1,dim_2,dim_3,j);
				end
				Constraint_4(dim_1,dim_2,dim_3,i) = sum_product;
			end
		end
	end
end
core = Constraint_4;

end