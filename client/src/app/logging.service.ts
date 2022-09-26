import { Injectable } from "@angular/core";
import { datadogLogs } from "@datadog/browser-logs";

@Injectable({
  providedIn: 'root'
})
export class LoggingService {

  constructor() { }

  logError(message: string, details: any, stack: string) {
      datadogLogs.logger.error(message, { applicationName: 'datadog-demo-client', ...details, stack })
  }

  logInfo(message: string, details: any) {
    datadogLogs.logger.info(message, { applicationName: 'datadog-demo-client', ...details})
  }
}
