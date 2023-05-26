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
    getAllFuture: builder.query({
      query: () => "event/getAllFuture",
    }),

    getScores: builder.query({
      query: (stageId) => `/score/getStageScores?stageId=${stageId}`,
    }),

    getSummedScores: builder.query({
      query: (args) =>
        `/score/getStagesSumScores?eventId=${args.eventId}&stageId=${args.stageId}`,
    }),
    getBasicTeams: builder.query({
      query: (eventId) => `/event/getBasicTeams?eventId=${eventId}`,
    }),
    getStatements: builder.query({
      query: (eventId) => `/statement/getStatements?eventId=${eventId}`,
    }),
    getStatementsCount: builder.query({
      query: (eventId) => `/statement/getStatementsCount?eventId=${eventId}`,
    }),
    getDriversCount: builder.query({
      query: (eventId) => `/event/getDriverCount?eventId=${eventId}`,
    }),
  }),
});

export const {
  useGetLogoPathByEventIdQuery,
  useGetAllBeforeQuery,
  useGetAllFutureQuery,
  useGetScoresQuery,
  useGetSummedScoresQuery,
  useGetBasicTeamsQuery,
  useGetStatementsQuery,
  useGetStatementsCountQuery,
  useGetDriversCountQuery,
} = rtkQueryApi;
