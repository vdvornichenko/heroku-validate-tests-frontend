//Storage for const variables that can be changed
export var HTTP_USER_CREDS_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/getUsers' : 'https://task-validation-lc.herokuapp.com/getUsers';
export var HTTP_USERS_INFO_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/usersInfo' : 'https://task-validation-lc.herokuapp.com/usersInfo';
export var HTTP_FILE_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/userFile' : 'https://task-validation-lc.herokuapp.com/userFile';
export var HTTP_FEEDBACK_URL = (window.location.href.includes('localhost')) ? 'http://localhost:8080/feedback' : 'https://task-validation-lc.herokuapp.com/feedback';

export const NOT_FOUND_MESSAGE = 'Не найден файл';
export const NO_SELECTED_USERS_MESSAGE = 'You must select at least 1 user';
export const ERRORS_NUMBER_MESSAGE = 'Количество ошибок - ';
export const ERROR_COLOR = '#EF5350';