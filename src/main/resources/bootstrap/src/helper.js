
export const connect = element => store => {
  Object.assign(element.prototype, { store })
}