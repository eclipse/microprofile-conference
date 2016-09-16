import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app.module';
import "rxjs-operators";
const platform = platformBrowserDynamic();
platform.bootstrapModule(AppModule);