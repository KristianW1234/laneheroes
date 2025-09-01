"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import MainPage from "@/components/MainPage";

export default function Main() {
  const router = useRouter();

  useEffect(() => {
    const user = localStorage.getItem("user");
    if (!user) {
      router.push("/login");
    }
  }, [router]);

  return <MainPage />;
}