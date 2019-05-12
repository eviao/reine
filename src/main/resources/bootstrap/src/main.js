import Vue from 'vue';
import HelloWorld from '@/components/HelloWorld.vue';

Vue.component('hello-world', HelloWorld);

new Vue({
    el: '#_reine_root',
    data: {
        message: 'Hello Vue!'
    }
});