const express = require("express");
const app = express();
app.use(express.json());

// Map of movies.
const movies = new Map();
var currId = 0;

// Simple validation
const validateMovie = (req, res, next) => {
  if (!req.body.title || !req.body.genre || !req.body.year) {
    return res.status(400).send("Title, genre, and year are required.");
  }
  next();
};

// GET all
app.get("/movies", (req, res) => {
  res.json(Array.from(movies.values()));
  console.log(`GET all: ${movies}`);
});

// GET by ID
app.get("/movies/:id", (req, res) => {
  const movieId = parseInt(req.params.id);
  const movie = movies.get(movieId);
  if (!movie) {
    return res.status(404).send("Movie not found");
  }

  res.json(movie);
  console.log(`GET id=${movieId}: ${movie}`);
});

// POST
app.post("/movies", validateMovie, (req, res) => {
  const movie = {
    id: currId,
    title: req.body.title,
    genre: req.body.genre,
    year: req.body.year,
  };
  movies.set(currId, movie);
  currId += 1;

  res.status(201).json(movie);
});

// PUT
app.put("/movies/:id", validateMovie, (req, res) => {
  const movieId = parseInt(req.params.id);
  const movie = movies.get(movieId);

  if (!movie) {
    return res.status(404).send("Movie not found!");
  }

  movie.title = req.body.title;
  movie.genre = req.body.genre;
  movie.year = req.body.year;

  res.json(movie);
});

// DELETE
app.delete("/movies/:id", (req, res) => {
  const movieId = parseInt(req.params.id);
  if (!movies.has(movieId)) {
    return res.status(404).send("Movie not found!");
  }

  const deletedMovie = movies.get(movieId);
  movies.delete(movieId);

  res.json(deletedMovie);
});

app.listen(8000, () => console.log("Server running on port 8000."));
