%==========================================================================
%   PROYECTO FINAL DE CARRERA - INGENIERIA INFORMATICA
%   FICH - UNL
%   2014/2015
%
%   ALUMNOS:
%       - Meurzet, Matías <matiasmeurzet@gmail.com>
%       - Perren, Leandro <leandroperren@gmail.com>
%
%   Funcion: 'energy'
%   Descripcion: esta funcion calcula la energía de la señal para la
%                ventana/frame que se está pasando como parámetro.
%
%   Entrada:
%       - x: vector con las muestras contenidas en la ventana.
%
%   Salida:
%       - e: energía de la ventana/frame en cuestion.
%
%==========================================================================

function e = energy(x)
    e = 0;
    N = length(x);
    for i=1:N
	    e = e + x(i)*x(i); 
    end
    e = e / N;
end
