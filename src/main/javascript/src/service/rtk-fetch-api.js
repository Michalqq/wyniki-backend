import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { backendUrl } from "../components/utils/fetchUtils";

export const rtkQueryApi = createApi({
  reducerPath: "rtkQueryApi",
  baseQuery: fetchBaseQuery({
    baseUrl: `${backendUrl()}/`,
    prepareHeaders: (headers) => {
      const username = sessionStorage.getItem("username");
      const token = sessionStorage.getItem("token");
      if (username && token) {
        headers.set("Authorization", `Bearer ${token}`);
      }

      return headers;
    },
  }),

  endpoints: (builder) => ({
    getLogoPathByEventId: builder.query({
      query: (eventId) => `event/getLogoPath?eventId=${eventId}`,
    }),

    getAllBefore: builder.query({
      query: () => "event/getAllBefore",
    }),

    getScores: builder.query({
      query: (stageId) => `/score/getStageScores?stageId=${stageId}`,
    }),

    getSummedScores: builder.query({
      query: (args) =>
        `/score/getStagesSumScores?eventId=${args.eventId}&stageId=${args.stageId}`,
    }),
  }),
});

export const {
  useGetLogoPathByEventIdQuery,
  useGetAllBeforeQuery,
  useGetScoresQuery,
  useGetSummedScoresQuery,
} = rtkQueryApi;
