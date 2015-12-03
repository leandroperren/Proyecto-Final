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
%   Descripcion: funciones de recorte usadas en el calculo del T0 y F0
%
%   Entrada:
%       - x: muestra de la señal
%       - cl: umbral de corte
%       - tipo: valores 'funcion1', 'funcion2' o 'funcion3'
%
%   Salida:
%       - y: valor de la muestra tras aplicar la funcion de recorte
%
%==========================================================================

function [y] = recorte(x, cl, tipo)
%funcion 1
if (tipo == 'funcion1')
    if (x >= cl)
       y=x-cl; 
    else
        if (x <= -cl)
            y=x+cl;
        else
            y=0; 
        end
    end
end
%funcion 2
if (tipo == 'funcion2')
    if (x >= cl)
       y=x; 
    else
        if ( x <= -cl)
            y=x;
        else
            y=0; 
        end
    end
end
%función 3
if (tipo == 'funcion3')
    if (x >= cl)
       y=x; 
    else
       y=0; 
    end
end
end

