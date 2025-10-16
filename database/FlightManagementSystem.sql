--
-- Database "FlightManagementSystem" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.7
-- Dumped by pg_dump version 14.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: FlightManagementSystem; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "FlightManagementSystem" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';


ALTER DATABASE "FlightManagementSystem" OWNER TO postgres;

--\connect "FlightManagementSystem"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: aircrafts_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aircrafts_table (
    aircraft_id character(10) NOT NULL,
    model character(15),
    category character(10),
    first_class_seats integer,
    business_class_seats integer,
    economy_class_seats integer
);


ALTER TABLE public.aircrafts_table OWNER TO postgres;

--
-- Name: airports_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.airports_table (
    airport_id bigint NOT NULL,
    airport_code character(4),
    airport_name character(50),
    city character(15),
    country character(15),
    continent character(15)
);


ALTER TABLE public.airports_table OWNER TO postgres;

--
-- Name: airports_table_airport_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.airports_table_airport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.airports_table_airport_id_seq OWNER TO postgres;

--
-- Name: airports_table_airport_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.airports_table_airport_id_seq OWNED BY public.airports_table.airport_id;


--
-- Name: bookings_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookings_table (
    booking_id bigint NOT NULL,
    booking_date timestamp with time zone,
    class_of_service character(15),
    departing_airport_id character(4),
    destination_airport_id character(4),
    flight_id bigint,
    seat_id bigint,
    customer_id bigint
);


ALTER TABLE public.bookings_table OWNER TO postgres;

--
-- Name: bookings_table_booking_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bookings_table_booking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bookings_table_booking_id_seq OWNER TO postgres;

--
-- Name: bookings_table_booking_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bookings_table_booking_id_seq OWNED BY public.bookings_table.booking_id;


--
-- Name: flights_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flights_table (
    flight_id bigint NOT NULL,
    departure_utc_time character(50),
    departing_airport_id character(4),
    destination_airport_id character(4),
    transit_airports_id text[],
    customers_list text[],
    seats_list text[],
    aircraft_id character(10)
);


ALTER TABLE public.flights_table OWNER TO postgres;

--
-- Name: flights_table_flight_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.flights_table_flight_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.flights_table_flight_id_seq OWNER TO postgres;

--
-- Name: flights_table_flight_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.flights_table_flight_id_seq OWNED BY public.flights_table.flight_id;


--
-- Name: seats_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seats_table (
    seat_id bigint NOT NULL,
    is_booked boolean,
    class_of_service character(15),
    flight_id bigint,
    seat_no character(5)
);


ALTER TABLE public.seats_table OWNER TO postgres;

--
-- Name: seats_table_seat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seats_table_seat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seats_table_seat_id_seq OWNER TO postgres;

--
-- Name: seats_table_seat_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.seats_table_seat_id_seq OWNED BY public.seats_table.seat_id;


--
-- Name: users_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_table (
    user_id bigint NOT NULL,
    email character(25),
    username character(12),
    password character(12),
    name character(30),
    passport_no character(9),
    status character(1),
    age character(3),
    user_type character(10)
);


ALTER TABLE public.users_table OWNER TO postgres;

--
-- Name: users_table_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_table_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_table_user_id_seq OWNER TO postgres;

--
-- Name: users_table_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_table_user_id_seq OWNED BY public.users_table.user_id;


--
-- Name: airports_table airport_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airports_table ALTER COLUMN airport_id SET DEFAULT nextval('public.airports_table_airport_id_seq'::regclass);


--
-- Name: bookings_table booking_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings_table ALTER COLUMN booking_id SET DEFAULT nextval('public.bookings_table_booking_id_seq'::regclass);


--
-- Name: flights_table flight_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flights_table ALTER COLUMN flight_id SET DEFAULT nextval('public.flights_table_flight_id_seq'::regclass);


--
-- Name: seats_table seat_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seats_table ALTER COLUMN seat_id SET DEFAULT nextval('public.seats_table_seat_id_seq'::regclass);


--
-- Name: users_table user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_table ALTER COLUMN user_id SET DEFAULT nextval('public.users_table_user_id_seq'::regclass);


--
-- Data for Name: aircrafts_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.aircrafts_table (aircraft_id, model, category, first_class_seats, business_class_seats, economy_class_seats) FROM stdin;
ARB112    	Embraer E190   	small     	5	20	50
ARB113    	Boeing 737 MAX 	medium    	6	24	120
ARB114    	Airbus A320neo 	medium    	8	30	150
\.


--
-- Data for Name: airports_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.airports_table (airport_id, airport_code, airport_name, city, country, continent) FROM stdin;
53	HEAT	Heathrow                                          	London         	UK             	Europe         
54	KATU	Katunayake                                        	Colombo        	Sri Lanka      	Asia           
55	CHEN	Chennai                                           	Chennai        	India          	Asia           
56	DUBA	Dubai                                             	Dubai          	UAE            	Asia           
57	TOKY	Tokyo International Airport                       	Tokyo          	Japan          	Asia           
58	SHAN	Shanghai Pudong Airport                           	Shanghai       	China          	Asia           
59	SEOU	Seoul Incheon Airport                             	Seoul          	South Korea    	Asia           
60	JAKA	Jakarta Soekarno-Hatta                            	Jakarta        	Indonesia      	Asia           
61	INDI	Indira Gandhi International Airport               	Delhi          	India          	Asia           
62	CHAR	Charles de Gaulle Airport                         	Paris          	France         	Europe         
63	ADOL	Adolfo Suárez Madrid–Barajas Airport              	Madrid         	Spain          	Europe         
64	LEON	Leonardo da Vinci–Fiumicino Airport               	Rome           	Italy          	Europe         
65	SHER	Sheremetyevo International Airport                	Moscow         	Russia         	Europe         
66	BERL	Berlin Tegel Airport                              	Berlin         	Germany        	Europe         
67	O'HA	O'Hare International Airport                      	Chicago        	USA            	America        
68	GEOR	George Bush Intercontinental Airport              	Houston        	USA            	America        
69	LOS 	Los Angeles International Airport                 	Los Angeles    	USA            	America        
70	PHOE	Phoenix Sky Harbor International Airport          	Phoenix        	USA            	America        
71	GUAR	Guarulhos International Airport                   	São Paulo      	Brazil         	America        
72	MINI	Ministro Pistarini International Airport          	Buenos Aires   	Argentina      	America        
73	JOHN	John F. Kennedy International Airport             	New York       	USA            	America        
74	CAIR	Cairo International Airport                       	Cairo          	Egypt          	Africa         
75	JOMO	Jomo Kenyatta International Airport               	Nairobi        	Kenya          	Africa         
76	MURT	Murtala Muhammed International Airport            	Lagos          	Nigeria        	Africa         
77	OR.T	OR.Tambo International Airport                    	Johannesburg   	South Africa   	Africa         
78	SYDN	Sydney Kingsford Smith Airport                    	Sydney         	Australia      	Australia      
79	PERT	Perth Airport                                     	Perth          	Australia      	Australia      
80	AUCK	Auckland Airport                                  	Auckland       	New Zealand    	Oceania        
\.


--
-- Data for Name: bookings_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookings_table (booking_id, booking_date, class_of_service, departing_airport_id, destination_airport_id, flight_id, seat_id, customer_id) FROM stdin;
1	2025-05-15 23:04:19.30916+05:30	FIRST          	KATU	HEAT	11	681	54
2	2025-05-15 23:27:04.134774+05:30	FIRST          	KATU	DUBA	11	676	54
3	2025-05-16 22:02:26.87837+05:30	BUSINESS       	KATU	CHEN	11	682	54
4	2025-05-16 22:04:53.795218+05:30	BUSINESS       	KATU	DUBA	11	683	54
5	2025-05-16 22:06:19.478269+05:30	BUSINESS       	KATU	DUBA	11	684	54
6	2025-05-16 22:08:05.294254+05:30	BUSINESS       	KATU	CHEN	11	685	54
7	2025-05-17 09:27:34.470493+05:30	BUSINESS       	KATU	DUBA	11	690	54
\.


--
-- Data for Name: flights_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flights_table (flight_id, departure_utc_time, departing_airport_id, destination_airport_id, transit_airports_id, customers_list, seats_list, aircraft_id) FROM stdin;
11	2025-05-30T15:00:00+05:30[Asia/Colombo]           	KATU	HEAT	{CHEN,DUBA}	{54,54,54}	{676,677,678,679,680,681,682,683,684,685,686,687,688,689,690,691,692,693,694,695,696,697,698,699,700,701,702,703,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,730,731,732,733,734,735,736,737,738,739,740,741,742,743,744,745,746,747,748,749,750}	ARB112    
12	2025-06-24T15:00:00+05:30[Asia/Colombo]           	SYDN	DUBA	{CHEN}	{}	{751,752,753,754,755,756,757,758,759,760,761,762,763,764,765,766,767,768,769,770,771,772,773,774,775,776,777,778,779,780,781,782,783,784,785,786,787,788,789,790,791,792,793,794,795,796,797,798,799,800,801,802,803,804,805,806,807,808,809,810,811,812,813,814,815,816,817,818,819,820,821,822,823,824,825}	ARB112    
\.


--
-- Data for Name: seats_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seats_table (seat_id, is_booked, class_of_service, flight_id, seat_no) FROM stdin;
677	f	FIRST          	11	F-002
678	f	FIRST          	11	F-003
679	f	FIRST          	11	F-004
680	f	FIRST          	11	F-005
686	f	BUSINESS       	11	B-006
687	f	BUSINESS       	11	B-007
688	f	BUSINESS       	11	B-008
689	f	BUSINESS       	11	B-009
691	f	BUSINESS       	11	B-011
692	f	BUSINESS       	11	B-012
693	f	BUSINESS       	11	B-013
694	f	BUSINESS       	11	B-014
695	f	BUSINESS       	11	B-015
696	f	BUSINESS       	11	B-016
697	f	BUSINESS       	11	B-017
698	f	BUSINESS       	11	B-018
699	f	BUSINESS       	11	B-019
700	f	BUSINESS       	11	B-020
701	f	ECONOMY        	11	E-001
702	f	ECONOMY        	11	E-002
703	f	ECONOMY        	11	E-003
705	f	ECONOMY        	11	E-005
706	f	ECONOMY        	11	E-006
707	f	ECONOMY        	11	E-007
708	f	ECONOMY        	11	E-008
709	f	ECONOMY        	11	E-009
710	f	ECONOMY        	11	E-010
711	f	ECONOMY        	11	E-011
712	f	ECONOMY        	11	E-012
713	f	ECONOMY        	11	E-013
714	f	ECONOMY        	11	E-014
715	f	ECONOMY        	11	E-015
716	f	ECONOMY        	11	E-016
717	f	ECONOMY        	11	E-017
718	f	ECONOMY        	11	E-018
719	f	ECONOMY        	11	E-019
720	f	ECONOMY        	11	E-020
721	f	ECONOMY        	11	E-021
722	f	ECONOMY        	11	E-022
723	f	ECONOMY        	11	E-023
724	f	ECONOMY        	11	E-024
725	f	ECONOMY        	11	E-025
726	f	ECONOMY        	11	E-026
727	f	ECONOMY        	11	E-027
728	f	ECONOMY        	11	E-028
729	f	ECONOMY        	11	E-029
730	f	ECONOMY        	11	E-030
731	f	ECONOMY        	11	E-031
732	f	ECONOMY        	11	E-032
733	f	ECONOMY        	11	E-033
734	f	ECONOMY        	11	E-034
735	f	ECONOMY        	11	E-035
736	f	ECONOMY        	11	E-036
737	f	ECONOMY        	11	E-037
738	f	ECONOMY        	11	E-038
739	f	ECONOMY        	11	E-039
740	f	ECONOMY        	11	E-040
741	f	ECONOMY        	11	E-041
742	f	ECONOMY        	11	E-042
743	f	ECONOMY        	11	E-043
744	f	ECONOMY        	11	E-044
745	f	ECONOMY        	11	E-045
746	f	ECONOMY        	11	E-046
747	f	ECONOMY        	11	E-047
748	f	ECONOMY        	11	E-048
749	f	ECONOMY        	11	E-049
750	f	ECONOMY        	11	E-050
681	t	BUSINESS       	11	B-001
676	t	FIRST          	11	F-001
704	t	ECONOMY        	11	E-004
682	t	BUSINESS       	11	B-002
683	t	BUSINESS       	11	B-003
684	t	BUSINESS       	11	B-004
685	t	BUSINESS       	11	B-005
690	t	BUSINESS       	11	B-010
751	f	FIRST          	12	F-001
752	f	FIRST          	12	F-002
753	f	FIRST          	12	F-003
754	f	FIRST          	12	F-004
755	f	FIRST          	12	F-005
756	f	BUSINESS       	12	B-001
757	f	BUSINESS       	12	B-002
758	f	BUSINESS       	12	B-003
759	f	BUSINESS       	12	B-004
760	f	BUSINESS       	12	B-005
761	f	BUSINESS       	12	B-006
762	f	BUSINESS       	12	B-007
763	f	BUSINESS       	12	B-008
764	f	BUSINESS       	12	B-009
765	f	BUSINESS       	12	B-010
766	f	BUSINESS       	12	B-011
767	f	BUSINESS       	12	B-012
768	f	BUSINESS       	12	B-013
769	f	BUSINESS       	12	B-014
770	f	BUSINESS       	12	B-015
771	f	BUSINESS       	12	B-016
772	f	BUSINESS       	12	B-017
773	f	BUSINESS       	12	B-018
774	f	BUSINESS       	12	B-019
775	f	BUSINESS       	12	B-020
776	f	ECONOMY        	12	E-001
777	f	ECONOMY        	12	E-002
778	f	ECONOMY        	12	E-003
779	f	ECONOMY        	12	E-004
780	f	ECONOMY        	12	E-005
781	f	ECONOMY        	12	E-006
782	f	ECONOMY        	12	E-007
783	f	ECONOMY        	12	E-008
784	f	ECONOMY        	12	E-009
785	f	ECONOMY        	12	E-010
786	f	ECONOMY        	12	E-011
787	f	ECONOMY        	12	E-012
788	f	ECONOMY        	12	E-013
789	f	ECONOMY        	12	E-014
790	f	ECONOMY        	12	E-015
791	f	ECONOMY        	12	E-016
792	f	ECONOMY        	12	E-017
793	f	ECONOMY        	12	E-018
794	f	ECONOMY        	12	E-019
795	f	ECONOMY        	12	E-020
796	f	ECONOMY        	12	E-021
797	f	ECONOMY        	12	E-022
798	f	ECONOMY        	12	E-023
799	f	ECONOMY        	12	E-024
800	f	ECONOMY        	12	E-025
801	f	ECONOMY        	12	E-026
802	f	ECONOMY        	12	E-027
803	f	ECONOMY        	12	E-028
804	f	ECONOMY        	12	E-029
805	f	ECONOMY        	12	E-030
806	f	ECONOMY        	12	E-031
807	f	ECONOMY        	12	E-032
808	f	ECONOMY        	12	E-033
809	f	ECONOMY        	12	E-034
810	f	ECONOMY        	12	E-035
811	f	ECONOMY        	12	E-036
812	f	ECONOMY        	12	E-037
813	f	ECONOMY        	12	E-038
814	f	ECONOMY        	12	E-039
815	f	ECONOMY        	12	E-040
816	f	ECONOMY        	12	E-041
817	f	ECONOMY        	12	E-042
818	f	ECONOMY        	12	E-043
819	f	ECONOMY        	12	E-044
820	f	ECONOMY        	12	E-045
821	f	ECONOMY        	12	E-046
822	f	ECONOMY        	12	E-047
823	f	ECONOMY        	12	E-048
824	f	ECONOMY        	12	E-049
825	f	ECONOMY        	12	E-050
\.


--
-- Data for Name: users_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_table (user_id, email, username, password, name, passport_no, status, age, user_type) FROM stdin;
53	sunil123@gmail.com       	Admin       	admin123    	-                             	-        	A	-  	admin     
54	adamsmith@example.com    	adamsmith   	adam123     	Adam Smith                    	P12344507	A	27 	customer  
56	saman@xyz.com            	saman_67    	saman123    	Saman Kumara                  	-        	A	-  	operator  
55	john.doe@example.com     	johndoe     	john123     	John Doe                      	P23545655	A	35 	customer  
\.


--
-- Name: airports_table_airport_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.airports_table_airport_id_seq', 80, true);


--
-- Name: bookings_table_booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bookings_table_booking_id_seq', 7, true);


--
-- Name: flights_table_flight_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.flights_table_flight_id_seq', 12, true);


--
-- Name: seats_table_seat_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seats_table_seat_id_seq', 825, true);


--
-- Name: users_table_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_table_user_id_seq', 56, true);


--
-- Name: aircrafts_table aircrafts_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aircrafts_table
    ADD CONSTRAINT aircrafts_table_pkey PRIMARY KEY (aircraft_id);


--
-- Name: airports_table airports_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airports_table
    ADD CONSTRAINT airports_table_pkey PRIMARY KEY (airport_id);


--
-- Name: bookings_table bookings_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings_table
    ADD CONSTRAINT bookings_table_pkey PRIMARY KEY (booking_id);


--
-- Name: flights_table flights_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flights_table
    ADD CONSTRAINT flights_table_pkey PRIMARY KEY (flight_id);


--
-- Name: seats_table seats_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seats_table
    ADD CONSTRAINT seats_table_pkey PRIMARY KEY (seat_id);


--
-- Name: users_table users_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_table
    ADD CONSTRAINT users_table_pkey PRIMARY KEY (user_id);


--
-- PostgreSQL database dump complete
--

