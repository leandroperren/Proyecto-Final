% NAME
%   spCorr - Auto-correlation of a signal (Correlogram)
% SYNOPSIS
%   [r] = spCorr(x, fs, maxlag, show)
% DESCRIPTION
%   Obtain Auto-correlation coefficients of a signal
% INPUTS
%   x        (vector) of size Nx1 which contains signal
%   fs       (scalar) the sampling frequency
%   [maxlag] (scalar) seek the correlation sequence over the lag range 
%             [-maxlag:maxlag]. Output r has length 2*maxlag+1.
%             The default is 20ms lag, that is, 50Hz (the minimum possible
%             F0 frequency of human speech)
% OUTPUTS
%   r        (vector) of size 2*maxlag+1 which contains 
%             correlation coefficients
% AUTHOR
%   Naotoshi Seo, April 2008
% USES
%   xcorr.m (Signal Processing toolbox)
function [r] = spCorrelum(x, fs, maxlag, show)
 %% Initialization
 if ~exist('maxlag', 'var') || isempty(maxlag)
     maxlag = fs/50; % F0 is greater than 50Hz => 20ms maxlag
 end
 if ~exist('show', 'var') || isempty(show)
     show = 0;
 end

 %% Auto-correlation
 r = xcorr(x, maxlag, 'coeff');

 fontSize = 16
 
 if show
     %% plot waveform
     t=(0:length(x)-1)/fs;        % times of sampling instants
     subplot(2,1,1);
     plot(t,x);
     legend('Oscilograma','FontSize',fontSize);
     xlabel('Tiempo (s)','FontSize',fontSize);
     ylabel('Amplitud','FontSize',fontSize);
     set(gca, 'fontsize', fontSize) 
     xlim([t(1) t(end)]);

     %% plot autocorrelation
     d=(-maxlag:maxlag)/fs;
     subplot(2,1,2);
     plot(d,r);
     legend('Auto correlaci�n','FontSize',fontSize);
     xlabel('Desplazamiento (s)','FontSize',fontSize);
     ylabel('Coeficientes de corr','FontSize',fontSize);
     set(gca, 'fontsize', fontSize) 
 end
end