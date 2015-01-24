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
%   Descripcion: este script sirve para leer los archivos con ronquidos
%   extraidos de las señales y calcular con ventanas deslizantes las
%   caracteristicas Energia, ZCR, RMS, factor cresta.
%
%   Entrada:
%       - Nombre del archivo .txt donde estan los ronquidos.
%
%   Salida:
%       - vectores ene, zcr, rms y crest, estos vectores guardan las
%   caracteristicas correspondientes al frame de la ventana deslizante.
%
%==========================================================================
clear all;

%Archivo a leer.
Dat = fopen('mujer_Lares_G_2011-11-07.EDF.txt','r');

%Tamaño de la ventana
L=10;

%Tamaño del solapamiento
paso = L/2;

i=1;
idx=1;

%Recorro el archivo linea por linea.
while ~feof(Dat)
   leer_linea = fgetl(Dat);
   if isempty(leer_linea) || ~ischar(leer_linea), break, end
   
   %Guardo la linea en una celda
   m{i}=leer_linea;
  
   %convierto los valores de la linea en numerico para calcular
   %caracteristicas
   x=str2num(m{i});
   
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
        
        %incremento el indice de los vectores
        idx = idx+1;
    end
   i=i+1; 
   
end