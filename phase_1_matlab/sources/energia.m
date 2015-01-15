%******************************************************************************
%
% energia.m
%
% Calcula la energia de un frame recibido como parametro de la funcion
%
% FICH-UNL
% Ingenieria Informatica 2015
%
% Meurzet, Matias <matiasmeurzet@gmail.com>
% Perren, Leandro <leandroperren@gmail.com>
%
%******************************************************************************

function e = energia(frame)

    e = 0;
    N = length(frame);

    for j=1:N
        e = e + frame(j)^2; 
    end

end

