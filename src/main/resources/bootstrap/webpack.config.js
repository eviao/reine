const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackInlineSourcePlugin = require('html-webpack-inline-source-plugin');

const resolve = (...paths) => path.resolve(__dirname, ...paths);

module.exports = {
  mode: 'development', // development or production
  entry: './src/bootstrap.js',
  output: {
    filename: 'bootstrap.js',
    path: resolve('dist'),
  },
  resolve: {
    alias: {
      '@': resolve('src'),
    },
  },
  plugins: [
    new HtmlWebpackPlugin({
      inlineSource: '.(js|css)$',
      template: 'src/template.tpl',
      filename: 'bootstrap.tpl',
      xhtml: true,
      inject: 'body',
    }),
    new HtmlWebpackInlineSourcePlugin(),
  ],
  module: {
    rules: [
      {
        test: /\.m?js$/,
        exclude: /(node_modules)/,
        use: {
          loader: 'babel-loader',
        }
      },
    ]
  }
};