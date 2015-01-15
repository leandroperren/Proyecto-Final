%==========================================================================
%   PROYECTO FINAL DE CARRERA - INGENIERIA INFORMATICA
%   FICH - UNL
%   2014/2015
%
%   ALUMNOS:
%       - Meurzet, Matías <matiasmeurzet@gmail.com>
%       - Perren, Leandro <leandroperren@gmail.com>
%
%
%   Funcion: 'ZCR'
%   Descripcion: esta funcion calcula la tasa de cruces por cero (zero crossing
%                rate) para la ventana bajo analisis.
%
%   Entrada:
%       - x: vector con las muestras contenidas en la ventana.
%
%   Salida:
%       - z: valor del algoritmo 'zcr'.
%
%==========================================================================

function z = ZCR(x)
z = 0;

for i=2:length(x)
    if x(i)*x(i-1) < 0
        z = z + 1;
    end
end
z = z / length(x);
end