/* eslint-disable react/display-name */
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import { backendUrl, checkReferee } from "../utils/fetchUtils";
import Button from "react-bootstrap/Button";
import { NewEventForm } from "../event/NewEventForm";
import { EventCard } from "../common/EventCard";
import Card from "react-bootstrap/Card";
import { TeamListModal } from "../team/TeamListModal";
import { EventModal } from "../event/EventModal";
import authHeader from "../../service/auth-header";
import { AdminTeamList } from "../team/AdminTeamList";
import Spinner from "react-bootstrap/Spinner";
import { StatementModal } from "../statement/StatementModal";

const HomePage = (props) => {
  const [futureEvents, setFutureEvents] = useState([]);
  const [archiveEvents, setArchiveEvents] = useState([]);
  const [createEvent, setCreateEvent] = useState();
  const [eventToTeamList, setEventToTeamList] = useState();
  const [eventToTeamPanel, setEventToTeamPanel] = useState();
  const [loadingFuture, setLoadingFuture] = useState(true);
  const [loadingBefore, setLoadingBefore] = useState(true);
  const [mainAdmin, setMainAdmin] = useState(false);
  const [redirected, setRedirected] = useState(false);
  const [showStatement, setShowStatement] = useState();
  const [referee, setReferee] = useState(false);

  let eventRedirect = useLocation().search;

  const navigate = useNavigate();

  const fetchEvents = () => {
    setLoadingFuture(true);
    setLoadingBefore(true);

    // let endDay = new Date();
    // endDay.setHours(23, 59, 59, 999);
    // endDay.setDate(new Date().getDate() - 1);

    // axios
    //   .get(`${backendUrl()}/event/getAll`, {
    //     headers: authHeader(),
    //   })
    //   .then((res) => {
    //     setFutureEvents(
    //       res.data
    //         .filter((x) => endDay.getTime() <= new Date(x.date).getTime())
    //         .sort((x, y) => (x.date < y.date ? -1 : 1))
    //     );
    //     setArchiveEvents(
    //       res.data
    //         .filter((x) => endDay.getTime() > new Date(x.date).getTime())
    //         .sort((x, y) => (x.date > y.date ? -1 : 1))
    //     );
    //     if (eventRedirect !== undefined && !redirected) {
    //       const event = res.data.find(
    //         (x) => x.eventId === Number(eventRedirect.replace("?", ""))
    //       );
    //       setEventToTeamPanel(event);
    //       eventRedirect = null;
    //       setRedirected(true);
    //     }
    //     setLoading(false);
    //   });

    axios
      .get(`${backendUrl()}/event/getAllFuture`, {
        headers: authHeader(),
      })
      .then((res) => {
        setFutureEvents(res.data.sort((x, y) => (x.date < y.date ? -1 : 1)));

        if (eventRedirect !== undefined && !redirected) {
          const index = eventRedirect.includes("&")
            ? eventRedirect.indexOf("&")
            : eventRedirect.length;
          const event = res.data.find(
            (x) =>
              x.eventId ===
              Number(eventRedirect.substring(0, index).replace("?", ""))
          );
          setEventToTeamPanel(event);
          eventRedirect = null;
          setRedirected(true);
        }
        setLoadingFuture(false);
      });

    axios
      .get(`${backendUrl()}/event/getAllBefore`, {
        headers: authHeader(),
      })
      .then((res) => {
        setArchiveEvents(res.data.sort((x, y) => (x.date > y.date ? -1 : 1)));
        setLoadingBefore(false);
      });
  };

  const fetchMainAdmin = () => {
    if (sessionStorage.getItem("username") !== null)
      axios
        .get(
          `${backendUrl()}/auth/isMainAdmin?login=${sessionStorage.getItem(
            "username"
          )}`,
          {
            headers: authHeader(),
          }
        )
        .then((res) => {
          setMainAdmin(res.data);
        });
  };

  useEffect(() => {
    fetchEvents();
    fetchMainAdmin();
  }, []);

  return (
    <>
      <Card
        style={{ borderRadius: "0" }}
        className=" my-1 bg-dark-green text-white green-header"
      >
        <h4 style={{ zIndex: 1 }} className="mb-1">
          Najbliższe wydarzenia
        </h4>
      </Card>
      <div className="row mx-0 justify-content-center card-body p-0">
        {loadingFuture && futureEvents.length === 0 && (
          <div className="text-center">
            <Spinner animation="border" variant="secondary" size="lg" />
          </div>
        )}
        {futureEvents.map((x) => (
          <EventCard
            key={x.eventId}
            event={x}
            onJoin={() => setEventToTeamPanel(x)}
            onScore={() => {
              localStorage.setItem("eventId", x.eventId);
              navigate("event", { state: { eventId: x.eventId } });
            }}
            onTeamList={() => {
              checkReferee(x.eventId, (data) => setReferee(data));
              setEventToTeamList(x);
            }}
            onStatement={() => setShowStatement(x)}
            onEdit={() => setCreateEvent(x)}
            mainAdmin={mainAdmin}
          />
        ))}
      </div>
      <div className="py-4"></div>
      <Card
        style={{ borderRadius: "0" }}
        className=" my-1 bg-dark-green text-white green-header"
      >
        <h4 style={{ zIndex: 1 }} className="mb-1">
          Archiwalne wydarzenia
        </h4>
      </Card>

      <div className="row mx-0 justify-content-center card-body">
        {loadingBefore && archiveEvents.length === 0 && (
          <div className="text-center">
            <Spinner animation="border" variant="secondary" size="lg" />
          </div>
        )}
        {archiveEvents.map((x) => (
          <EventCard
            key={x.eventId}
            event={x}
            onJoin={() => setEventToTeamPanel(x)}
            onScore={() => {
              localStorage.setItem("eventId", x.eventId);
              navigate("event", { state: { eventId: x.eventId } });
            }}
            onTeamList={() => setEventToTeamList(x)}
            onStatement={() => setShowStatement(x)}
            onEdit={() => setCreateEvent(x)}
            mainAdmin={mainAdmin}
          />
        ))}
      </div>
      <div className="p-3 border-top">
        {mainAdmin && (
          <Button
            className={"border-top mx-3"}
            variant="primary"
            onClick={() => setCreateEvent(true)}
          >
            Dodaj wydarzenie
          </Button>
        )}
      </div>
      <NewEventForm
        show={createEvent !== undefined}
        handleClose={() => {
          setCreateEvent();
          fetchEvents();
        }}
        event={createEvent}
      />
      {(mainAdmin || referee) && (
        <AdminTeamList
          show={eventToTeamList !== undefined}
          handleClose={() => {
            setEventToTeamList();
            fetchEvents();
          }}
          eventId={eventToTeamList?.eventId}
          eventName={eventToTeamList?.name}
          started={eventToTeamList?.started}
        />
      )}
      {!mainAdmin && !referee && (
        <TeamListModal
          show={eventToTeamList !== undefined}
          handleClose={() => {
            setEventToTeamList();
            fetchEvents();
          }}
          eventId={eventToTeamList?.eventId}
          started={eventToTeamList?.started}
        />
      )}
      {showStatement && (
        <StatementModal
          show={true}
          handleClose={() => setShowStatement()}
          event={showStatement}
        />
      )}
      <EventModal
        show={eventToTeamPanel !== undefined}
        handleClose={() => {
          setEventToTeamPanel();
          fetchEvents();
        }}
        event={eventToTeamPanel}
      />
    </>
  );
};

export default HomePage;
