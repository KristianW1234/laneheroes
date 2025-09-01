"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    const user = localStorage.getItem("user");

    if (user) {
      router.push("/main"); // or your authenticated main page
    } else {
      router.push("/login"); // fallback if not logged in
    }
  }, [router]);

  return null; // or a loading spinner
}