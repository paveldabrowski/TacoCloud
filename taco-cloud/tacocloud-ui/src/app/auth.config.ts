// To API pojawi się w następnej wersji
import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {

  // Adres URL dostawcy tożsamości
  issuer: 'https://localhost:8080/oauth2/login', // TODO: DO POPRAWY... TO JEST ZROBIONE ŹLE

  // Adres URL strony, na którą użytkownik będzie przekierowany po zalogowaniu się
  redirectUri: window.location.origin + '/index.html',

  // Adres URL strony, na którą użytkownik będzie przekierowany po cichym odświeżeniu strony
  silentRefreshRedirectUri: window.location.origin + '/silent-refresh.html',

  // Identyfikator strony. W omawianym przykładzie to serwer uwierzytelniania
  clientId: 'spa-demo',

  // Zdefiniowanie zasięgu uprawnień dla żądania klienta
  // Pierwsze trzy są zdefiniowane przez OIDC, zaś czwarte jest charakterystyczne dla aplikacji
  scope: 'openid profile email voucher',

  showDebugInformation: true,

  sessionChecksEnabled: true
};
