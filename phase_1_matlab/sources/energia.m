function [ e ] = energia(frame)
%función que devuelve la energia de un vector frame.
e=0;    
for j=1:length(frame)
           e=e+(frame(j)^2); 
end

end

