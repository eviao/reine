import _ from 'lodash'

class Store {
  constructor(data) {
    this.data = data || {}
  }
  get = path => _.get(this.data, path)
}

class Bootstrap {
  constructor(data) {
    this.store = new Store(data)
    this.initComponents()
  }

  initComponents() {
    const context = require.context('./components', false, /[A-Z]\w+\.js$/)
    context.keys().forEach(fileName => {
      const config = context(fileName)
      const module = config.default || config
      if (_.isFunction(module)) {
        module(this.store)
      }
    })
  }
}

new Bootstrap(window._reineData)