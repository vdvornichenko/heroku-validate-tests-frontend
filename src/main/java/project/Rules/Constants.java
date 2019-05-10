package project.Rules;

public class Constants {
//   for TRIGGER
    public static final String TRIGGER_OBJECT_SUCCESS_MESSAGE = "Триггер создан для нужного объекта";
    public static final String TRIGGER_OBJECT_ERROR_MESSAGE = "Триггер создан не для требуемого объекта";
    public static final String TRIGGER_EVENT_ERROR_MESSAGE = "В триггере не использовано событие ";
    public static final String TRIGGER_EVENT_SUCCESS_MESSAGE = " события использованы";
    public static final String TRIGGER_HELPER_SUCCESS_MESSAGE = "Вспомогательный класс создан и использован";
    public static final String TRIGGER_HELPER_ERROR_MESSAGE = "Не создан вспомогательный класс или неправильно назван";
//   for TEST
    public static final String TEST_FAILED =  "Failed Test: {0}";
    public static final String TEST_SUCCESS_BUT_NOT_ENOUGH_COVERAGE =  "Tests: {0} SUCCESS but not enough coverage: {1} %";
    public static final String TEST_SUCCESS =  "Tests: {0} SUCCESS with coverage: {1} %";
//   for ApexClass
    public static final String APEXCLASS_NOT_FOUND_METHOD =  "У класса: {0} не найден метод: {1}";
    public static final String APEXCLASS_FOUND_METHOD =  "Найден метод: {1} у класса: {0}";
//   for sObject
    public static final String SOBJECT_NOT_FOUND_FIELD =  "Не найдено поле: {0} у обьекта: {1}";
    public static final String SOBJECT_FOUND_FIELD =  "Найдено поле: : {0} у обьекта: {1}";
    public static final String SOBJECT_NOT_FOUND__PROPERTY =  "Не корректное свойство: {0}  у поля  {1} у обьекта  {2} ";
    public static final String SOBJECT_FOUND__PROPERTY =  "Корректное свойство: {0}  у поля  {1} у обьекта  {2} ";
    public static final String SOBJECT_NOT_FOUND_VALIDATIONRULES =  "Не найдены validationRules: {0} у обьекта: {1} ";
    public static final String SOBJECT_FOUND_VALIDATIONRULES =  "Найдены validationRules: {0} у обьекта: {1}";
    public static final String SOBJECT_WRONG_VALIDATIONRULES_FORMULA =  "Некоректная формула у validationRules: {0}, не найдено поле: {1}";




}
