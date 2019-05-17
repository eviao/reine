import { LitElement } from 'lit-element'

function isExpression(str) {
  return /^\s*{{.*}}\s*$/.test(str)
}

function resolveExpression(el) {
  return el.replace(/{{/, '')
    .replace(/}}/, '')
    .replace(/\s/g, '')
}

export class Component extends LitElement {
  
  static get properties() {
    return {
      value: { noAccessor: true }
    }
  }
  
  constructor() {
    super()
  }
  
  set value(val) {
    const { store, _value } = this
    const oldVal = _value
    if (isExpression(val) && store) {
      const path = resolveExpression(val)
      this._value = store.get(path)
    } else {
      this._value = val
    }
    this.requestUpdate('value', oldVal)
  }
  
  get value() { return this._value }
}