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
%   Descripcion: esta script permite leer una señal EDF y obtener T0 y F0
%
%   Entrada:
%       - Ruta de la señal EDF
%       - m_ini: número de muestra inicial a analizar
%       - m_fin: número de muestra final a analizar
%
%   Salida:
%       - T0: Período fundamental.
%       - F0: Frecuencia fundamental.
%
%==========================================================================
clear all;
source_file = 'Apnealink/hombres/Beber_I_2011-10-26.EDF';

% lectura de la señal
[hdr, datos] = edfread(source_file);

% definicion de la muestra inicial para comenzar el analisis


%ronquido
duracion = 6000;
 
% m_ini = 1333200; 
%   
% m_ini = 1381300;

% m_ini = 2121100;

m_ini = 1823900;

m_fin = m_ini+duracion;


x = [m_ini, m_fin];
% fragmento de ronquido 1min -> 6000 muestras.
ronquido = datos(2, m_ini:m_fin);

% cantidad de muestras en vector 'ronquido'
n = length(ronquido);

%obtengo el maximo de la señal
[m, idx] = max(ronquido);

%factor de recorte en porcentaje
CL = 2.55;
CL=m*0.1;

for i=1:n
%     if (ronquido(i) < 1.2)
%        ronquido(i) = 0; 
%     end
%     ronquido(i) = recorte(ronquido(i), CL, 'funcion3');
end

r = spCorrelum(ronquido, 100, [1000], 'plot');

[f0 maxi]= spPitchCorr(r, 100)
t0 = 1/f0

t0_inf = 3.20;
    t0_sup = 5.90;
    
    if ((t0>t0_inf) && (t0< t0_sup))
        if (maxi > 0.04)
            salida=1
        else
           salida=0
        end
    else
       salida=0
    end

