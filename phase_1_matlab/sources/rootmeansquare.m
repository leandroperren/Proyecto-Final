%==========================================================================
%   PROYECTO FINAL DE CARRERA - INGENIERIA INFORMATICA
%   FICH - UNL
%   2014/2015
%
%   ALUMNOS:
%       - Meurzet, Mat�as <matiasmeurzet@gmail.com>
%       - Perren, Leandro <leandroperren@gmail.com>
%
%
%   Funcion: 'rms'
%   Descripcion: esta funcion calcula el root mean square de la se�al para la
%                ventana/frame que se est� pasando como par�metro.
%
%   Entrada:
%       - x: vector con las muestras contenidas en la ventana.
%
%   Salida:
%       - rms: rms de la ventana/frame en cuestion.
%
%==========================================================================

function rms = rootmeansquare(x)
rms = 0;
N = length(x);
for i=1:N
	rms = rms + x(i)*x(i); 
end
rms = (rms / N)^(1/2);
end