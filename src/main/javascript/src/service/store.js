import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/query";
import { rtkQueryApi } from "./rtk-fetch-api";

export const store = configureStore({
  reducer: {
    [rtkQueryApi.reducerPath]: rtkQueryApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(rtkQueryApi.middleware),
});

setupListeners(store.dispatch);
