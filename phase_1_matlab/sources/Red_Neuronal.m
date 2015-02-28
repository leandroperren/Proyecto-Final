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
%   Descripcion: este script sirve para crear una red neuronal del
%   tipo Feedforward.
%
%   Entrada:
%       - Nombre del archivo .txt de la base de datos de caracteristicas.
%
%   Salida:
%       - red neuronal entrenada.
%
%==========================================================================

clear all;

%Leo el archivo de base de datos de caracteristicas para el entrenamiento.
[ene,zcr,rms,cres,enezcr,salida]=textread('reales/BaseDatosCaracteristicas.txt','%f %f %f %f %f %f');

%Guardo los datos para el entrenamiento en la matriz P.
P=[ene'; zcr'; rms'; cres'; enezcr'];

%Guardo los datos de la salida deseada.
T=salida';

%Genero la red neuronal.
net = newff([0 1; 0 1; 0 1; 0 1; 0 1],[5 1],{'logsig' 'logsig'});

%Defino parámetros para el entrenamiento.
net.trainParam.epochs = 2000;

%Entreno la red neuronal (Entreno con los 150 primeros y el resto para pruebas).
net = train(net,P(:,1:150),T(1:150));

%Prueba la red con datos no usados en el entrenamiento.
 Y = sim(net,P(:,150:227));
 
 %Grafico los resultados obtenidos y los reales.
 subplot(2,1,1);
 stem(Y);
 xlabel('Nro frame');
 ylabel('Resultado obtenido');
 subplot(2,1,2);
 stem(T(150:227));
 xlabel('Nro frame');
 ylabel('Resultado deseado');
