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

source_file = 'Apnealink/hombres/Beber_I_2011-10-26.EDF';

% lectura de la señal
[hdr, datos] = edfread(source_file);

% definicion de la muestra inicial para comenzar el analisis
%limitacion
% m_ini = 1086000;
% m_fin = 1092000;

% m_ini = 1371800;
% m_fin = 1377800;

% m_ini = 2348500;
% m_fin = 2354500;

% m_ini = 2088200;
% m_fin = 2094200;

%ronquido
% m_ini = 1823900;
% m_fin = 1829900;
 
% m_ini = 1333200; 
% m_fin = 1339200;
% 
% m_ini = 1326400;
% m_fin = 1332400;

%ronquido y limitacion mezclados
% m_ini = 2160300;
% m_fin = 2166300;

% m_ini = 2190300;
% m_fin = 2196300;

% m_ini = 762900; %00:37:09<->00:38:09
% m_fin = 768900;

x = [m_ini, m_fin];
% fragmento de ronquido 1min -> 6000 muestras.
ronquido = datos(2, m_ini:m_fin);

% cantidad de muestras en vector 'ronquido'
n = length(ronquido);

%obtengo el maximo de la señal
[m, idx] = max(ronquido);

%factor de recorte en porcentaje
factor = 0.1;
CL = m*factor;

for i=1:n
    ronquido(i) = recorte(ronquido(i), CL, 'funcion3');
end

%calculo la autocorrelación
r = spCorrelum(ronquido, 100, [6000], 'plot');

%caracteristicas
f0 = spPitchCorr(r, 100)
t0 = 1/f0



