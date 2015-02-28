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
%   Descripcion: este script sirve para leer las señales con ronquidos
%   reales y calcular con ventanas deslizantes las
%   caracteristicas Energia, ZCR, RMS, factor cresta, Energia/ZCR.
%
%   Entrada:
%       - Nombre del archivo .wav donde estan los ronquidos.
%
%   Salida:
%       - archivo .txt  donde se guardan las caracteristicas 
%       correspondientes al frame de la ventana deslizante.
%
%==========================================================================

clear all;

%Leo la señal wav.
[x, fs] = wavread('reales/ronquido_16000hz_8.wav');

%Tamaño de la ventana
L=1600;

%Tamaño del solapamiento
paso = L/2;

i=1;
idx=1;

%Abro el archivo donde guardo caracteristicas
fid = fopen('reales/caracteristicas8.txt','w');

%para cada linea calcula por frame las caracteristicas
for i = 1 : paso : length(x)-L
    % frame actual bajo analisis
    frame = x(i : i+L)

    % calculo de energia
    ene(idx) = energy(frame)/length(frame);

    % calculo de ZCR
    zcr(idx) = ZCR(frame);
        
    %calculo el rms
    rms(idx) = rootmeansquare(frame);
        
    %calculo el factor cresta
    crest(idx) = crestFactor(frame);
       
    %cociente energia/zcr
    if (zcr(idx)==0)
        enezcr(idx) = 0;
    else
        enezcr(idx) = ene(idx)/zcr(idx);
    end
        
    %guardo las carcateristicas en un archivo (cada fila es un frame)
    fila = [ene(idx) zcr(idx) rms(idx) crest(idx)];
    fprintf(fid, '%d ', ene(idx));
    fprintf(fid, '%d ', zcr(idx));
    fprintf(fid, '%d ', rms(idx));
    fprintf(fid, '%d ', crest(idx));
    fprintf(fid, '%d ', enezcr(idx));
    fprintf(fid, '%d ', 1);
    fprintf(fid, '\n');
        
    %incremento el indice de los vectores
    idx = idx+1;
end

fclose(fid);