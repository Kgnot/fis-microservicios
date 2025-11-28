terraform {
  required_version = ">= 1.5.0"

  cloud {
    organization = "UNIVERSIDAD_UD"

    workspaces {
      name = "fis-microservicios"
    }
  }

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  credentials = file("./terraform-admin.json") # para local, ignorado en TFC
  project     = var.GOOGLE_PROJECT
  region      = var.GOOGLE_REGION
}

resource "google_compute_instance" "free_vm" {
  name         = "rolapet-vm"
  machine_type = "e2-micro"
  zone         = "${var.GOOGLE_REGION}-b"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-12"
    }
  }

  network_interface {
    network = "default"

    access_config {
      // IP pública
    }
  }

  metadata_startup_script = file("${path.module}/startup.sh")

  tags = ["http-server", "https-server"]
}

resource "google_compute_firewall" "allow_all" {
  name    = "allow-http-https"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80", "443", "22"]
  }

  source_ranges = ["0.0.0.0/0"]
}
