clear all;
clc;

% Tiempo inicial
tic;

% Frecuencia de la señal [muestras/seg]
Fs = 100;

source_file = 'señal.EDF';
% lectura de la señal
[hdr, datos] = edfread(source_file);

% definicion de la muestra inicial para comenzar el analisis
m_ini = 540001;
m_fin = 545000;

% fragmento de ronquido 10seg -> 1000 muestras.
ronquido = datos(2, m_ini:m_fin);

% cantidad de muestras en vector 'ronquido'
n = length(ronquido);

% fragmento de flujo
flujo = datos(1, m_ini:m_fin);

% ventana de 100ms -> 10 muestras.
% CALCULO: 100[Hz] = 100[muestras/seg]*1/1000[seg/mseg] = 1/10[muestras/mseg]
% multiplicando el calculo anterior por la cantidad de milisegundos
% necesarios y luego aplicando la funcion 'floor' se obtiene el tamaño de
% la ventana que se necesite
L = floor((Fs/1000)*100);

% indice para vector 'ene' y 'zcr' (numero de frame bajo analisis)
idx = 1;

% avanzo a razon de L/2 por solapamiento 50ms
paso = floor(L/2);

% reservacion de memoria para vector 'ene' y 'zcr'
cantidad_frames = (n-L)/(L-paso);
ene = zeros(1, cantidad_frames);
zcr = zeros(1, cantidad_frames);

for i = 1 : paso : length(ronquido)-L
    
    % frame actual bajo analisis
    frame = ronquido(i : i+L);
    
    % calculo de energia
    ene(idx) = energia(frame)/length(frame);
    
    % calculo de ZCR
    zcr(idx) = ZCR(frame);
    
    idx = idx+1;
end

% Umbral de decisión
T = 60;

% Identificacion de episodios respecto al umbral
% 'i' es el número de frame o ventana.

% reservacion de memoria para vector 'seg'
seg = zeros(1, cantidad_frames);

for i = 1 : length(ene)

   if zcr(i) > 0 && ene(i)/zcr(i) > T
      seg(i) = 1;
   else
      seg(i) = 0;
   end
end


% Calculo de relacion energia/zcr
rel = zeros(1, cantidad_frames);

for k = 1 : cantidad_frames
    if zcr(k) == 0
        rel(k) = 0;
    else
        rel(k) = ene(k)/zcr(k);
    end
end


% separacion de episodios detectados y almacenamiento en celda de salida
i = 1;

% numero de episodio
n_epi = 0;

% memoria para celda 'salida'
salida = cell(1, cantidad_frames);

%recorro los frames en busca de los episodios detectados.

while (i <= length(seg))
   cont = 0;
   inicio = i;

   if seg(i) == 1
       % Encontrando el primer frame detectado sigue hasta el ultimo
       while seg(i) == 1 && i<length(seg)
           cont = cont+1;
           i = i+1;
       end
       % extraccion del episodio
       comienzo = (inicio-1)*(L/2) + 1;
       fin      = (cont+inicio-1)*(L/2) + (L/2);
       
       %%episodio = ronquido(comienzo : fin);
       episodio = [comienzo fin];
       
       % numero de episodio
       n_epi = n_epi+1;
       
       % guardado de episodio en celda de salida
       salida{n_epi} = episodio;
   end
   
   i = i+1;
end

% Guardado de episodios en archivo *.txt
fid = fopen('salida.txt','w');
[f, c] = size(salida);

for i = 1 : c
   fila = salida{i};
   for j = 1 : length(fila)
       fprintf(fid, '%d ', fila(j));
   end
   fprintf(fid, '\n');
end

% Rutinas de graficación
figure(1);

% ploteo de 'ronquido' y episodios detectados
subplot(2,1,1);
plot(1:n, ronquido(1:n), 1:n, flujo(1:n), 'g')
xlabel('Muestra');
ylabel('Amplitud');

subplot(2,1,2);
fill(1:cantidad_frames, seg, 'r')
xlabel('Frame');
ylabel('Detectado[1] - No Detectado[0]');
axis xy
ylim([0 2])

figure(2);
% ploteo del ronquido
subplot(3,1,1);
plot(ronquido(1:n))
xlabel('Muestra');
ylabel('Amplitud');

% ploteo de energia para cada frame
subplot(3,1,2);
plot(ene)
xlabel('Frame');
ylabel('Energia');

% ploteo de zcr para cada frame
subplot(3,1,3);
plot(zcr)
xlabel('Frame');
ylabel('ZCR');


figure(3)
% ploteo de señal original
subplot(2,1,1)
plot(ronquido)
xlabel('Muestra')
ylabel('Amplitud')

% ploteo de relacion energia/zcr
subplot(2,1,2);
plot(rel, 'k')
xlabel('Frame')
ylabel('Tasa Adimensional [Energia/ZCR]')
hold on
plot(T*ones(1, cantidad_frames), 'r', 'LineWidth', 1.00)
hold off
legend('Energia/ZCR', ['Umbral: ' num2str(T)])


% Tiempo final
toc;
