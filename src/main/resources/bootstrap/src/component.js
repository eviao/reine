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
      value: { type: String }
    }
  }
  
  constructor() {
    super()
  }
  
  getValue() {
    const { store, value } = this
    if (isExpression(value) && store) {
      const path = resolveExpression(value)
      return store.get(path)
    } else {
      return value
    }
  }
}