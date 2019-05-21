import StartPage from '../components/StartPage.vue';
import TaskMapping from '../components/TaskMapping/TaskMappingContainer.vue';
import App from '../App.vue';
export const routes = [
    {
        path: '/',
        name: "Home",
        component:StartPage
    }, {
        path: '/taskCreator',
        name: "taskCreator",
        component:TaskMapping   
    }
];
