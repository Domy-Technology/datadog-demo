import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { datadogLogs } from '@datadog/browser-logs';

if (environment.production) {
  enableProdMode();
}

datadogLogs.init({
    clientToken: 'pub60ae817ea16096147e780c63f1f2ff6b',
    site: 'datadoghq.com',
    forwardErrorsToLogs: true,
    sampleRate: 100
});

platformBrowserDynamic()
  .bootstrapModule(AppModule)
  .catch(err => console.error(err));
