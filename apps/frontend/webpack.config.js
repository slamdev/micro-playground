const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const webpack = require('webpack');

module.exports = {
    entry: {
        app: [
            'babel-polyfill',
            'react-hot-loader/patch',
            'webpack/hot/only-dev-server',
            './src/index.js'
        ]
    },

    plugins: [
        new CleanWebpackPlugin(['build']),
        new HtmlWebpackPlugin({template: 'src/index.html'}),
        new webpack.NamedModulesPlugin(),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'node-static',
            filename: 'node-static.js',
            minChunks(module, count) {
                const context = module.context;
                return context && context.indexOf('node_modules') >= 0;
            },
        })
    ],

    devtool: 'inline-source-map',

    devServer: {
        contentBase: './build'
    },

    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'build')
    },

    module: {
        rules: [
            {
                enforce: 'pre',
                test: /\.js$/,
                exclude: /node_modules/,
                use: ['eslint-loader']
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            ['env', {modules: false}],
                            'react', 'stage-2'],
                        plugins: ['react-hot-loader/babel']
                    }
                }
            },
            {test: /\.css$/, use: ['style-loader', 'css-loader']}
        ]
    }
};
