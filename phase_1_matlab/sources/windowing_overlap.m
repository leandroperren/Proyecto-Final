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
%   Funcion: 'windowing_overlap'
%   Descripcion: esta funcion calcula las muestras que sobran y faltan para
%                luego aplicar alguna politica sobre ello.
%
%   Entrada:
%       - samples: cantidad de muestras que se quieren ventanear con
%                  solapamiento para realizar los calculos.
%       - ancho_ventana: cantidad de muestras que contiene la ventana.
%       - solapamiento: cantidad de muestras que se solapan en cada avance
%                       de la ventana.
%
%   Salida:
%       - sobran: cantidad de muestras que sobran (es casi irrelevante).
%       - faltan: cantidad de muestras que faltan para poder aplicar otra
%                 ventana completa.
%
%==========================================================================

function [sobran, faltan] = windowing_overlap(samples, ancho_ventana, solapamiento)

avance = ancho_ventana - solapamiento;
sobran = samples;
faltan = 0;
%idx = 1;            % indice de la muestra
%cant_ventanas = 0;

while sobran > ancho_ventana
   sobran = sobran - avance;
%    idx = idx + avance;
%    cant_ventanas = cant_ventanas + 1;
end

if sobran == ancho_ventana
    sobran = 0;
%    idx = idx + avance;
%    cant_ventanas = cant_ventanas + 1;
else
    if sobran < ancho_ventana
        faltan = ancho_ventana - sobran;
    end
end

% DESCOMENTAR ESTOS if SI SE QUIERE IMPRIMIR CUANTO SOBRA O FALTA
% if sobran > 0
%     fprintf('Sobran %d muestras\n', sobran);
% end

% if faltan ~= 0
%     fprintf('Faltan %d muestras\n', faltan);
% end
