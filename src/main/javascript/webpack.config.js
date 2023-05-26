const BundleAnalyzerPlugin =
  require("webpack-bundle-analyzer").BundleAnalyzerPlugin;
const MomentLocalesPlugin = require("moment-locales-webpack-plugin");
const path = require("path");

module.exports = {
  output: {
    filename: "main.[contenthash].js",
    clean: true,
  },
  devtool: "source-map",
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        loader: "babel-loader",
        options: {
          presets: ["@babel/preset-env", "@babel/preset-react"],
        },
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"],
        //exclude: path.resolve("./node_modules/bootstrap"),
      },
    ],
  },
  plugins: [
    new BundleAnalyzerPlugin(),
    new MomentLocalesPlugin({
      localesToKeep: ["pl"],
    }),
  ],
  resolve: {
    extensions: [".ts", ".js", ".jsx", ".css"],
    modules: ["node_modules", "src/main/javascript"],
  },
  mode: "production",
};
