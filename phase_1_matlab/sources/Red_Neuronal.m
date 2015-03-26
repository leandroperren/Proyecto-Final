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
net = newff(minmax(P),[4 1],{'logsig' 'logsig'});

%Defino parámetros para el entrenamiento.
net.trainParam.epochs = 2000;

%Entreno la red neuronal (Entreno con los 150 primeros y el resto para pruebas).
[net error]= train(net,P(:,1:150),T(1:150));

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

%calculo los porcentajes de aciertos
cantidad = length(Y);
acierto = 0;
falsos_positivos = 0;
falsos_negativos = 0;
T=T(150:227);
for i=1:1:length(Y)
    %mapeo las salidas a 0 y 1 (función signo).
    if (Y(i)<= 0.5)
        Y(i) = 0;
    else
        Y(i) = 1;
    end
    %calculo los falsos positivos
    if ((Y(i) == 1) && (T(i) == 0))
        falsos_positivos = falsos_positivos +1;
    end
    %calculo los falsos negativos
    if ((Y(i) == 0) && (T(i) == 1))
        falsos_negativos = falsos_negativos +1;
    end
    %calculo la cantidad de aciertos
    if (Y(i)==T(i))
        acierto=acierto+1;
    end
end
disp('Aciertos:');
(acierto/cantidad)*100
disp('Falsos positivos:');
(falsos_positivos/cantidad)*100
disp('Falsos negativos:');
(falsos_negativos/cantidad)*100
