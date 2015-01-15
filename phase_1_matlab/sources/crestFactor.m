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
%   Funcion: 'crestFactor'
%   Descripcion: esta funcion calcula el factor cresta de la señal para la
%                ventana/frame que se está pasando como parámetro.
%
%   Entrada:
%       - x: vector con las muestras contenidas en la ventana.
%
%   Salida:
%       - crest: factor cresta de la ventana/frame en cuestion.
%
%==========================================================================

function crest = crestFactor(x)

crest = max(abs(x))/rootmeansquare(x);

end