const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackInlineSourcePlugin = require('html-webpack-inline-source-plugin');
const VueLoaderPlugin = require('vue-loader/lib/plugin');

const resolve = (...paths) => path.resolve(__dirname, ...paths);

module.exports = {
  mode: 'development', // development or production
  entry: './src/main.js',
  output: {
    filename: 'bootstrap.js',
    path: resolve('dist'),
  },
  resolve: {
    alias: {
      'vue$': resolve('node_modules', 'vue/dist/vue.esm.js'),
      '@': resolve('src'),
    },
  },
  plugins: [
    new VueLoaderPlugin(),
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
        test: /\.vue$/,
        loader: 'vue-loader',
      },
      {
        test: /\.m?js$/,
        exclude: /(node_modules)/,
        use: {
          loader: 'babel-loader',
        }
      },
      {
        test: /\.css$/,
        use: [
          'vue-style-loader',
          'style-loader',
          'css-loader',
        ],
      },
    ]
  }
};