//Storage for const variables that can be changed
export var HTTP_USER_CREDS_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/getUsers' : 'https://task-validation-lc.herokuapp.com/getUsers';
export var HTTP_USERS_INFO_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/usersInfo' : 'https://task-validation-lc.herokuapp.com/usersInfo';
export var HTTP_FILE_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/userFile' : 'https://task-validation-lc.herokuapp.com/userFile';
export var HTTP_FEEDBACK_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/feedback' : 'https://task-validation-lc.herokuapp.com/feedback';
export var HTTP_AUTHORIZATION_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/authorization' : 'https://task-validation-lc.herokuapp.com/authorization';

export const NOT_FOUND_MESSAGE = 'Не найден файл';
export const NO_SELECTED_USERS_MESSAGE = 'Выберите хотя бы одного пользователя';
export const ERRORS_NUMBER_MESSAGE = 'Количество ошибок - ';
export const ERROR_COLOR = '#EF5350';
export const NO_FEEDBACK_MESSAGE = 'Введите что-нибудь';
export const FEEDBACK_SUCCESS_MESSAGE = 'Отзыв отправлен';
export const FEEDBACK_ERROR_MESSAGE = 'Произошла ошибка при отправке на сервер';
export const AUTH_TOKEN = 'task-validation-vue-js-spring-vrp-company';
