package com.epam.smartcityhousingsystem.exceptions.handlers;

import com.epam.smartcityhousingsystem.exceptions.*;
import com.epam.smartcityhousingsystem.exceptions.responses.CustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler is the class that provides all exception
 * handling methods.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(value = AdvertisementArchiveNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleAdvertisementArchiveNotFoundException(AdvertisementArchiveNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Advertisement archive not found"));
    }

    @ExceptionHandler(value = AdvertisementNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleAdvertisementArchiveNotFoundException(AdvertisementNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Advertisement  not found"));
    }
    @ExceptionHandler(value = AdvertisementNotOnSaleException.class)
    public ResponseEntity<CustomExceptionResponse> handleAdvertisementNotOnSaleException(AdvertisementNotOnSaleException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Advertisement is not on sale"));
    }

    @ExceptionHandler(value = ConfirmationPasswordDoesNotMatchException.class)
    public ResponseEntity<CustomExceptionResponse> handleConfirmationPasswordDoesNotMatchException(ConfirmationPasswordDoesNotMatchException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Confirmation password does not match with the password"));
    }

    @ExceptionHandler(value = CustomAuthenticationException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomAuthenticationException(CustomAuthenticationException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomExceptionResponse("Bad credentials are provided "));
    }

    @ExceptionHandler(value = PaymentNotSuccessfulException.class)
    public ResponseEntity<CustomExceptionResponse> handlePaymentNotSuccessfulException(PaymentNotSuccessfulException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Payment is not successful"));
    }

    @ExceptionHandler(value = ResidentDoesNotOwnTheAdvertisementException.class)
    public ResponseEntity<CustomExceptionResponse> handleResidentDoesNotOwnTheAdvertisementException(ResidentDoesNotOwnTheAdvertisementException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Resident does not own the advertisement"));
    }

    @ExceptionHandler(value = ResidentDoesNotOwnTheHouseException.class)
    public ResponseEntity<CustomExceptionResponse> handleResidentDoesNotOwnTheHouseException(ResidentDoesNotOwnTheHouseException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Resident does not own the house"));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("User not found"));
    }

    @ExceptionHandler(value = ResidentNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleResidentNotFoundException(ResidentNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Resident not found"));
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleRoleNotFoundException(RoleNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Role not found"));
    }

    @ExceptionHandler(value = HouseNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleHouseNotFoundException(HouseNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("House not found"));
    }

    @ExceptionHandler(value = UserHasAlreadySignedUpException.class)
    public ResponseEntity<CustomExceptionResponse> handleUserHasAlreadySignedUpException(UserHasAlreadySignedUpException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("User has already signed up"));
    }

    @ExceptionHandler(value = MoneyTransferWaitingNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleMoneyTransferWaitingNotFoundException(MoneyTransferWaitingNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Money transfer waiting not found"));
    }


    @ExceptionHandler(value = HousingSystemConfirmationNotSuccessful.class)
    public ResponseEntity<CustomExceptionResponse> handleHousingSystemConfirmationNotSuccessful(HousingSystemConfirmationNotSuccessful e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse("Housing system confirmation is not successful>>> " + e.getMessage()));
    }

    @ExceptionHandler(value = PostForMoneyTransferException.class)
    public ResponseEntity<CustomExceptionResponse> handlePostForMoneyTransferException(PostForMoneyTransferException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(value = PostForConfirmationFromCityAdministrationException.class)
    public ResponseEntity<CustomExceptionResponse> handlePostForConfirmationFromCityAdministrationException(PostForConfirmationFromCityAdministrationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(value = GetFinalResponseException.class)
    public ResponseEntity<CustomExceptionResponse> handleGetFinalResponseException(GetFinalResponseException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage()));
    }
}
