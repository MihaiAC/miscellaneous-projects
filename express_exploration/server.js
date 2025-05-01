const express = require("express");
const mongoose = require("mongoose");
const { v4: uuidv4 } = require("uuid");
const Movie = require("./models/Movie");
const app = express();
app.use(express.json());

mongoose
  .connect("mongodb://localhost:27017/moviesdb")
  .then(() => console.log("MongoDB connected"))
  .catch((err) => console.error("MongoDB connection error:", err));

// Map of movies.
const movies = new Map();

// Simple validation
const validateMovie = (req, res, next) => {
  if (!req.body.title || !req.body.genre || !req.body.year) {
    return res.status(400).send("Title, genre, and year are required.");
  }
  next();
};

// GET all
app.get("/movies", async (req, res) => {
  const allMovies = await Movie.find();
  res.json(allMovies);
});

// GET by ID
app.get("/movies/:id", async (req, res) => {
  console.log(`Received ID: ${req.params.id}`);
  try {
    const movie = await Movie.findOne({ _id: String(req.params.id) });
    if (!movie) {
      return res.status(404).send("Movie not found");
    }
    res.json(movie);
  } catch {
    res.status(400).send("Invalid ID");
  }
});

// POST
app.post("/movies", validateMovie, async (req, res) => {
  try {
    const movie = await Movie.create({
      _id: uuidv4(),
      title: req.body.title,
      genre: req.body.genre,
      year: req.body.year,
    });

    res.status(201).json(movie);
  } catch (err) {
    res.status(500).send("Error saving movie");
  }
});

// PUT
app.put("/movies/:id", validateMovie, async (req, res) => {
  try {
    const movie = await Movie.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
    });
    if (!movie) {
      return res.status(404).send("Movie not found!");
    }
    res.json(movie);
  } catch {
    res.status(400).send("Invalid ID");
  }
});

// DELETE
app.delete("/movies/:id", async (req, res) => {
  try {
    const movie = await Movie.findByIdAndDelete(req.params.id);
    if (!movie) {
      return res.status(404).send("Movie not found");
    }
    res.json(movie);
  } catch {
    res.status(400).send("Invalid ID");
  }
});

app.listen(8000, () => console.log("Server running on port 8000."));
