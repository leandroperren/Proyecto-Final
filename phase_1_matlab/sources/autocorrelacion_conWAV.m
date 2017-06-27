clear all;

%calcular el T0 de un audio .wav


%leo la señal del sonido ambiental
[datos, fs] = wavread('reales/sonido_ambiente4.wav');

% [datos, fs] = wavread('ronquidos.wav');
%submuestreo a 100 Hz
datos = downsample(datos, 160);
fs = 100;

% definicion de la muestra inicial para comenzar el analisis
%limitacion


%ronquido
duracion = 30000;

m_ini = 1;
m_fin = length(datos);

%ronquido y limitacion mezclados




ronquido = datos(m_ini:m_fin);

% cantidad de muestras en vector 'ronquido'
n = length(ronquido);

%obtengo el maximo de la señal
[m, idx] = max(ronquido);

%factor de recorte en porcentaje
factor = 0.1;
CL = 2.3;
CL=m*0.1;

for i=1:n
%     ronquido(i) = recorte(ronquido(i), CL, 'funcion3');
end

r = spCorrelum(ronquido, 100, [1000], 'plot');

[f0 maxi]= spPitchCorr(r, 100)
t0 = 1/f0
