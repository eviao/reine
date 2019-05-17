import { customElement, html, css } from 'lit-element';
import { Component } from '@/component';
import { connect } from '@/helper';

@customElement('hello-world')
class HelloWorld extends Component {

  constructor() {
    super()
  }

  static styles = css`
    span {
      color: red;
    }`;

  render() {
    return html`Web Components are <span>${this.value}</span>!`
  }
}

export default connect(HelloWorld)