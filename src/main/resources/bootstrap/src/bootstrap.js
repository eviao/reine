import _ from 'lodash'

const componentsContext = require.context('./components', false, /[A-Z]\w+\.js$/)

class Store {
    constructor(data) {
        this.data = data || {}
    }
    get = path => _.get(this.data, path)
}

class Bootstrap {
    constructor(data) {
        this.store = new Store(data)
        this.initComponents();
    }

    initComponents() {
        componentsContext.keys().forEach(fileName => {
            const config = componentsContext(fileName)
            const module = config.default || config
            if (_.isFunction(module)) {
                module(this.store)
            }
        })
    }
}