const CompressionPlugin = require("compression-webpack-plugin");

module.exports = {
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
        test: /\.css$/i,
        use: ["style-loader", "css-loader"],
      },
    ],
  },
  plugins: [
    new CompressionPlugin({
      test: /\.js(\?.*)?$/i,
    }),
  ],
  performance: {
    hints: "warning",
    // Calculates sizes of gziped bundles.
    assetFilter: function (assetFilename) {
      return assetFilename.endsWith(".js.gz");
    },
  },
  resolve: {
    extensions: [".ts", ".js", ".jsx", ".css"],
    modules: ["node_modules", "src/main/javascript"],
  },
  mode: "development",
  optimization: {
    usedExports: true,
  },
};
