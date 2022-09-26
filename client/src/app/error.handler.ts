import { HttpErrorResponse } from "@angular/common/http";
import { ErrorHandler, Injectable, Injector } from "@angular/core";
import { LoggingService } from "./logging.service";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

    constructor(private injector: Injector) { }

    handleError(error: Error | HttpErrorResponse) {

        const logger = this.injector.get(LoggingService);
        let message;
        let stackTrace;

        if (error instanceof HttpErrorResponse) {
            // Server Error
            message = error.message;
            stackTrace = 'stack';
        } else {
            // Client Error
            message = error.message ? error.message : error.toString();
            stackTrace = error.stack;
        }

        logger.logError(message, {}, stackTrace ?? '');
        console.error(error);
    }

}
