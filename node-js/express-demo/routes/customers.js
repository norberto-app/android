const express = require("express");
const mongoose = require("mongoose");
const auth = require("../middleware/auth");
const admin = require("../middleware/admin");
const { Customer, validate } = require("../models/customer");

const router = express.Router();

router.get("/", async (req, res) => {
  throw new Error('Could not get the request customer');

  res.send(await Customer.find().sort("name"));
});

router.get("/:id", async (req, res) => {
  const id = req.params.id;
  const customer = await Customer.findById(id);

  if (customer) {
    res.send(customer);
  } else {
    res.status(404).send("The customer with the given Id was not found");
  }
});

router.post("/", auth, async (req, res) => {
  const { error } = validate(req.body);

  if (error) {
    res.status(400).send(error.details[0].message);
  } else {
    const customer = new Customer({
      name: req.body.name,
      phone: req.body.phone,
      isGold: req.body.isGold,
    });

    res.send(await customer.save());
  }
});

router.put("/:id", auth, async (req, res) => {
  const { error } = validate(req.body);

  if (error) {
    res.status(400).send(error.details[0].message);
  } else {
    try {
      // Find the existing course object.
      const id = req.params.id;

      console.log("caca " + id);
      const customer = await Customer.findByIdAndUpdate(
        id,
        {
          name: req.body.name,
          phone: req.body.phone,
          isGold: req.body.isGold,
        },
        { new: true }
      );

      if (customer) {
        res.send(customer);
      } else {
        res.status(404).send("The customer with the given Id does not exist.");
      }
    } catch (ex) {
      res.status(404).send(ex);
    }
  }
});

router.delete("/:id", [auth, admin], async (req, res) => {
  const id = req.params.id;
  const customer = await Customer.findByIdAndRemove(id);

  if (customer) {
    res.send(customer);
  } else {
    res.status(404).send("The customer with the given Id was not found");
  }
});

module.exports = router;
