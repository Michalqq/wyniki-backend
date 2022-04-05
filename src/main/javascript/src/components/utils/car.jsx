export const getCarLogo = (carBrand, height) => {
  let brand = carBrand?.toLowerCase().replace(/ /g, "");

  if (brand === "vw") brand = "volkswagen";

  const path = `https://vehapi.com/img/car-logos/${brand}.png`;

  return (
    <img
      className="img-fluid"
      style={{ height: height + "px" }}
      src={path}
      alt=""
    ></img>
  );
};
