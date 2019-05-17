import { customElement, html, css } from 'lit-element'
import { Component } from '@/component'
import { connect } from '@/helper'

@customElement('hello-world')
class HelloWorld extends Component {

  constructor() {
    super()
  }

  static styles = css`
    span {
      color: red;
    }`

  render() {
    return html`hello, <span>${this.getValue()}</span>!`
  }
}

export default connect(HelloWorld)