package com.example.lolmanager.model;

import com.example.lolmanager.comparator.StartListComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SwsxTournament {
    private Title maxNorm = Title.bk;
    private boolean rematch = false;
    private String name;
    private Date startDate;
    private Date endDate;
    private String arbiter;
    private String organizer;
    private String place;
    private Tournament.TournamentSystem system;
    private String arbiterEmail;
    private String organizerEmail;
    private byte roundsNo;
    private byte currentRound;
    private short eloFloor;
    private byte minGamesForElo;
    private Float pointsForForfeitWin = 1.0F;
    private Float pointsForForfeitLose = 0F;
    private Float pointsForBye = 1.0F;
    private Float pointsForHalfBye = 0.5F;
    private boolean twoFederations;
    private byte minGamesForTitle;
    private Tournament.Tiebreak.TbMethod tiebreak0;
    private Tournament.Tiebreak.TbMethod tiebreak1;
    private Tournament.Tiebreak.TbMethod tiebreak2;
    private Tournament.Tiebreak.TbMethod tiebreak3;
    private Tournament.Tiebreak.TbMethod tiebreak4;
    private StartListComparator.SortCriteria sort0;
    private StartListComparator.SortCriteria sort1;
    private StartListComparator.SortCriteria sort2;
    private StartListComparator.SortCriteria sort3;
    private StartListComparator.SortCriteria sort4;
    private ArrayList<SwsxPlayer> players = new ArrayList<>();
    private TournamentReportPol reportPol;
    private TournamentReportFide reportFide;
    private String rate;

    public SwsxTournament(File file) {
        String xmlFileName = "tournament.xml";
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry entry = zipFile.getEntry(xmlFileName);

            if (entry != null) {
                InputStream is = zipFile.getInputStream(entry);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document document = builder.parse(is);

                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();

                Element maxNormNode = (Element) document.getElementsByTagName("norm_idx_compute").item(0);
                Element rematchNode = (Element) document.getElementsByTagName("tour_match_rematch").item(0);
                Element nameNode = (Element) document.getElementsByTagName("tour_name").item(0);
                Element startDateNode = (Element) document.getElementsByTagName("start_date").item(0);
                Element endDateNode = (Element) document.getElementsByTagName("end_date").item(0);
                Element arbiterNode = (Element) document.getElementsByTagName("arbiter").item(0);
                Element organizerNode = (Element) document.getElementsByTagName("organiz").item(0);
                Element placeNode = (Element) document.getElementsByTagName("place").item(0);
                Element systemNode = (Element) document.getElementsByTagName("system_of_play").item(0);
                Element arbiterEmailNode = (Element) document.getElementsByTagName("arbiter_email").item(0);
                Element organizerEmailNode = (Element) document.getElementsByTagName("organiz_email").item(0);
                Element roundsNoNode = (Element) document.getElementsByTagName("no_of_round").item(0);
                Element currentRoundNode = (Element) document.getElementsByTagName("current_round").item(0);
                Element eloFloorNode = (Element) document.getElementsByTagName("low_thres_elo").item(0);
                Element minGamesForEloNode = (Element) document.getElementsByTagName("min_no_games_for_elo").item(0);
                Element pointsForForfeitWinNode = (Element) document.getElementsByTagName("forfeit_points").item(0);
                Element pointsForForfeitLoseNode = (Element) document.getElementsByTagName("forfeit_subst_points").item(0);
                Element pointsForByeNode = (Element) document.getElementsByTagName("bye_points").item(0);
                Element twoFederationsNode = (Element) document.getElementsByTagName("disable_fed_laws_for_fide_title_norms").item(0);
                Element minGamesForTitleNode = (Element) document.getElementsByTagName("min_no_games_for_fide_title_norms").item(0);
                Element tiebreak0Node = (Element) document.getElementsByTagName("tiebreak0").item(0);
                Element tiebreak1Node = (Element) document.getElementsByTagName("tiebreak1").item(0);
                Element tiebreak2Node = (Element) document.getElementsByTagName("tiebreak2").item(0);
                Element tiebreak3Node = (Element) document.getElementsByTagName("tiebreak3").item(0);
                Element tiebreak4Node = (Element) document.getElementsByTagName("tiebreak4").item(0);
                Element sort0Node = (Element) document.getElementsByTagName("sort0").item(0);
                Element sort1Node = (Element) document.getElementsByTagName("sort1").item(0);
                Element sort2Node = (Element) document.getElementsByTagName("sort2").item(0);
                Element sort3Node = (Element) document.getElementsByTagName("sort3").item(0);
                Element sort4Node = (Element) document.getElementsByTagName("sort4").item(0);
                Element reportPolNode = (Element) document.getElementsByTagName("tournament_report_pol").item(0);
                Element reportFideNode = (Element) document.getElementsByTagName("report_FIDE_data").item(0);
                Element rateNode = (Element) document.getElementsByTagName("rate_play").item(0);

                String maxNorm = maxNormNode.getAttribute("value");
                String rematch = rematchNode.getAttribute("value");
                String name = nameNode.getAttribute("value");
                String startDate = startDateNode.getAttribute("year") + "/" + startDateNode.getAttribute("month") + "/" + startDateNode.getAttribute("day");
                String endDate = endDateNode.getAttribute("year") + "/" + endDateNode.getAttribute("month") + "/" + endDateNode.getAttribute("day");
                String arbiter = arbiterNode.getAttribute("value");
                String organizer = organizerNode.getAttribute("value");
                String place = placeNode.getAttribute("value");
                String system = systemNode.getAttribute("value");
                String arbiterEmail = arbiterEmailNode.getAttribute("value");
                String organizerEmail = organizerEmailNode.getAttribute("value");
                String roundsNo = roundsNoNode.getAttribute("value");
                String currentRound = currentRoundNode.getAttribute("value");
                String eloFloor = eloFloorNode.getAttribute("value");
                String minGamesForElo = minGamesForEloNode.getAttribute("value");
                String pointsForForfeitWin = pointsForForfeitWinNode.getAttribute("value");
                String pointsForForfeitLose = pointsForForfeitLoseNode.getAttribute("value");
                String pointsForBye = pointsForByeNode.getAttribute("value");
                String twoFederations = twoFederationsNode.getAttribute("value");
                String minGamesForTitle = minGamesForTitleNode.getAttribute("value");
                String tiebreak0 = tiebreak0Node.getAttribute("value");
                String tiebreak1 = tiebreak1Node.getAttribute("value");
                String tiebreak2 = tiebreak2Node.getAttribute("value");
                String tiebreak3 = tiebreak3Node.getAttribute("value");
                String tiebreak4 = tiebreak4Node.getAttribute("value");
                String sort0 = sort0Node.getAttribute("value");
                String sort1 = sort1Node.getAttribute("value");
                String sort2 = sort2Node.getAttribute("value");
                String sort3 = sort3Node.getAttribute("value");
                String sort4 = sort4Node.getAttribute("value");
                String rate = rateNode.getAttribute("value");

                switch (maxNorm) {
                    case "6" -> setMaxNorm(Title.M);
                    case "5" -> setMaxNorm(Title.K);
                    case "4" -> setMaxNorm(Title.I);
                    case "3" -> setMaxNorm(Title.II);
                    case "2" -> setMaxNorm(Title.III);
                    case "1" -> setMaxNorm(Title.IV);
                    default -> setMaxNorm(Title.V);
                }
                setRematch(rematch.equals("1"));
                setName(name);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    setStartDate(dateFormat.parse(startDate));
                } catch (ParseException e) {
                    setStartDate(dateFormat.parse("1899/12/31"));
                }
                try {
                    setEndDate(dateFormat.parse(endDate));
                } catch (ParseException e) {
                    setEndDate(dateFormat.parse("1899/12/31"));
                }
                setArbiter(arbiter);
                setOrganizer(organizer);
                setPlace(place);
                setSystem(!system.toLowerCase().contains("robin") ? Tournament.TournamentSystem.SWISS : Tournament.TournamentSystem.ROUND_ROBIN);
                setArbiterEmail(arbiterEmail);
                setOrganizerEmail(organizerEmail);
                try {
                    setRoundsNo(Byte.parseByte(roundsNo));
                } catch (NumberFormatException e) {
                    setRoundsNo((byte) 0);
                }
                try {
                    setCurrentRound(Byte.parseByte(currentRound));
                } catch (NumberFormatException e) {
                    setCurrentRound((byte) 0);
                }
                try {
                    setEloFloor(Short.parseShort(eloFloor));
                } catch (NumberFormatException e) {
                    setEloFloor((short) 1000);
                }
                try {
                    setMinGamesForElo(Byte.parseByte(minGamesForElo));
                } catch (NumberFormatException e) {
                    setMinGamesForElo((byte) 5);
                }
                try {
                    setPointsForForfeitWin(Float.parseFloat(pointsForForfeitWin.replaceAll(",", ".")));
                } catch (NumberFormatException e) {
                    setPointsForForfeitWin(1F);
                }
                try {
                    setPointsForForfeitLose(-Float.parseFloat(pointsForForfeitLose.replaceAll(",", ".")));
                } catch (NumberFormatException e) {
                    setPointsForForfeitLose(0F);
                }
                try {
                    setPointsForBye(Float.parseFloat(pointsForBye.replaceAll(",", ".")));
                } catch (NumberFormatException e) {
                    setPointsForBye(1F);
                }
                setPointsForHalfBye(getPointsForBye() / 2);
                setTwoFederations(twoFederations.equals("1"));
                try {
                    setMinGamesForTitle(Byte.parseByte(minGamesForTitle));
                } catch (NumberFormatException e) {
                    setMinGamesForTitle((byte) 5);
                }
                setTiebreak0(Tournament.Tiebreak.TbMethod.getTbMethod(tiebreak0));
                setTiebreak1(Tournament.Tiebreak.TbMethod.getTbMethod(tiebreak1));
                setTiebreak2(Tournament.Tiebreak.TbMethod.getTbMethod(tiebreak2));
                setTiebreak3(Tournament.Tiebreak.TbMethod.getTbMethod(tiebreak3));
                setTiebreak4(Tournament.Tiebreak.TbMethod.getTbMethod(tiebreak4));
                setSort0(StartListComparator.SortCriteria.getSortCriteria(sort0));
                setSort1(StartListComparator.SortCriteria.getSortCriteria(sort1));
                setSort2(StartListComparator.SortCriteria.getSortCriteria(sort2));
                setSort3(StartListComparator.SortCriteria.getSortCriteria(sort3));
                setSort4(StartListComparator.SortCriteria.getSortCriteria(sort4));
                setReportPol(new TournamentReportPol(reportPolNode));
                setReportFide(new TournamentReportFide(reportFideNode));
                setRate(rate);

                XPathExpression expression = xPath.compile(".//list_of_players/cobarray_item");
                NodeList cobarrayItems = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < cobarrayItems.getLength(); i++) {
                    Element cobarrayItem = (Element) cobarrayItems.item(i);
                    SwsxPlayer player = new SwsxPlayer(cobarrayItem);
                    players.add(player);
                }
                is.close();
            } else {
                System.err.println("The specified XML file was not found in the ZIP archive.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true); // Make private fields accessible
                String fieldName = field.getName();
                Object fieldValue = field.get(this);
                result.append(fieldName).append(": ").append(fieldValue).append("\n");
            }
        } catch (IllegalAccessException ignored) {
        }
        return result.toString();
    }

    public Title getMaxNorm() {
        return maxNorm;
    }

    public void setMaxNorm(Title maxNorm) {
        this.maxNorm = maxNorm;
    }

    public boolean isRematch() {
        return rematch;
    }

    public void setRematch(boolean rematch) {
        this.rematch = rematch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getArbiter() {
        return arbiter;
    }

    public void setArbiter(String arbiter) {
        this.arbiter = arbiter;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Tournament.TournamentSystem getSystem() {
        return system;
    }

    public void setSystem(Tournament.TournamentSystem system) {
        this.system = system;
    }

    public String getArbiterEmail() {
        return arbiterEmail;
    }

    public void setArbiterEmail(String arbiterEmail) {
        this.arbiterEmail = arbiterEmail;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public byte getRoundsNo() {
        return roundsNo;
    }

    public void setRoundsNo(byte roundsNo) {
        this.roundsNo = roundsNo;
    }

    public byte getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(byte currentRound) {
        this.currentRound = currentRound;
    }

    public short getEloFloor() {
        return eloFloor;
    }

    public void setEloFloor(short eloFloor) {
        this.eloFloor = eloFloor;
    }

    public byte getMinGamesForElo() {
        return minGamesForElo;
    }

    public void setMinGamesForElo(byte minGamesForElo) {
        this.minGamesForElo = minGamesForElo;
    }

    public Float getPointsForForfeitWin() {
        return pointsForForfeitWin;
    }

    public void setPointsForForfeitWin(Float pointsForForfeitWin) {
        this.pointsForForfeitWin = pointsForForfeitWin;
    }

    public Float getPointsForForfeitLose() {
        return pointsForForfeitLose;
    }

    public void setPointsForForfeitLose(Float pointsForForfeitLose) {
        this.pointsForForfeitLose = pointsForForfeitLose;
    }

    public Float getPointsForBye() {
        return pointsForBye;
    }

    public void setPointsForBye(Float pointsForBye) {
        this.pointsForBye = pointsForBye;
    }

    public Float getPointsForHalfBye() {
        return pointsForHalfBye;
    }

    public void setPointsForHalfBye(Float pointsForHalfBye) {
        this.pointsForHalfBye = pointsForHalfBye;
    }

    public boolean isTwoFederations() {
        return twoFederations;
    }

    public void setTwoFederations(boolean twoFederations) {
        this.twoFederations = twoFederations;
    }

    public byte getMinGamesForTitle() {
        return minGamesForTitle;
    }

    public void setMinGamesForTitle(byte minGamesForTitle) {
        this.minGamesForTitle = minGamesForTitle;
    }

    public Tournament.Tiebreak.TbMethod getTiebreak0() {
        return tiebreak0;
    }

    public void setTiebreak0(Tournament.Tiebreak.TbMethod tiebreak0) {
        this.tiebreak0 = tiebreak0;
    }

    public Tournament.Tiebreak.TbMethod getTiebreak1() {
        return tiebreak1;
    }

    public void setTiebreak1(Tournament.Tiebreak.TbMethod tiebreak1) {
        this.tiebreak1 = tiebreak1;
    }

    public Tournament.Tiebreak.TbMethod getTiebreak2() {
        return tiebreak2;
    }

    public void setTiebreak2(Tournament.Tiebreak.TbMethod tiebreak2) {
        this.tiebreak2 = tiebreak2;
    }

    public Tournament.Tiebreak.TbMethod getTiebreak3() {
        return tiebreak3;
    }

    public void setTiebreak3(Tournament.Tiebreak.TbMethod tiebreak3) {
        this.tiebreak3 = tiebreak3;
    }

    public Tournament.Tiebreak.TbMethod getTiebreak4() {
        return tiebreak4;
    }

    public void setTiebreak4(Tournament.Tiebreak.TbMethod tiebreak4) {
        this.tiebreak4 = tiebreak4;
    }

    public StartListComparator.SortCriteria getSort0() {
        return sort0;
    }

    public void setSort0(StartListComparator.SortCriteria sort0) {
        this.sort0 = sort0;
    }

    public StartListComparator.SortCriteria getSort1() {
        return sort1;
    }

    public void setSort1(StartListComparator.SortCriteria sort1) {
        this.sort1 = sort1;
    }

    public StartListComparator.SortCriteria getSort2() {
        return sort2;
    }

    public void setSort2(StartListComparator.SortCriteria sort2) {
        this.sort2 = sort2;
    }

    public StartListComparator.SortCriteria getSort3() {
        return sort3;
    }

    public void setSort3(StartListComparator.SortCriteria sort3) {
        this.sort3 = sort3;
    }

    public StartListComparator.SortCriteria getSort4() {
        return sort4;
    }

    public void setSort4(StartListComparator.SortCriteria sort4) {
        this.sort4 = sort4;
    }

    public ArrayList<SwsxPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<SwsxPlayer> players) {
        this.players = players;
    }

    public TournamentReportPol getReportPol() {
        return reportPol;
    }

    public void setReportPol(TournamentReportPol reportPol) {
        this.reportPol = reportPol;
    }

    public TournamentReportFide getReportFide() {
        return reportFide;
    }

    public void setReportFide(TournamentReportFide reportFide) {
        this.reportFide = reportFide;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public class TournamentReportPol {
        private Arbiter chiefArbiter;
        private String state;
        private String rateOfPlay;
        private ArrayList<SwsxEvent> schedule = new ArrayList<>();
        private ArrayList<Arbiter> arbiters = new ArrayList<>();

        TournamentReportPol(Element report) throws XPathExpressionException {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            Element chiefArbiterNameNode = (Element) report.getElementsByTagName("chief_arbiter_name").item(0);
            Element chiefArbiterTitleNode = (Element) report.getElementsByTagName("chief_arbiter_title").item(0);
            Element chiefArbiterLicenseNode = (Element) report.getElementsByTagName("chief_arbiter_lic").item(0);
            Element rateNode = (Element) report.getElementsByTagName("rate_of_play_it").item(0);
            Element stateNode = (Element) report.getElementsByTagName("state_number").item(0);
            NodeList scheduleListNode = (NodeList) report.getElementsByTagName("schedule_list").item(0);

            setChiefArbiter(
                    new Arbiter(
                            chiefArbiterNameNode.getAttribute("value"),
                            ArbiterTitle.getAribterTitle(chiefArbiterTitleNode.getAttribute("value")),
                            chiefArbiterLicenseNode.getAttribute("value")
                    )
            );
            setState(stateNode.getAttribute("value"));
            setRateOfPlay(rateNode.getAttribute("value"));

            for (int i = 0; i < scheduleListNode.getLength(); i++) {
                Element cobarrayItem = (Element) scheduleListNode.item(i);
                schedule.add(new SwsxEvent(cobarrayItem));
            }

            XPathExpression namesExpression = xPath.compile(".//arbiters/cstring");
            XPathExpression titlesExpression = xPath.compile(".//arbiters_title/cstring");
            XPathExpression licensesExpression = xPath.compile(".//arbiters_licence/cstring");
            XPathExpression workExpression = xPath.compile(".//arbiters_work_description/cstring");

            NodeList nameList = (NodeList) namesExpression.evaluate(report, XPathConstants.NODESET);
            NodeList titleList = (NodeList) titlesExpression.evaluate(report, XPathConstants.NODESET);
            NodeList licenseList = (NodeList) licensesExpression.evaluate(report, XPathConstants.NODESET);
            NodeList workList = (NodeList) workExpression.evaluate(report, XPathConstants.NODESET);

            for (int i = 0;
                 i < Integer.min(
                         Integer.min(nameList.getLength(), titleList.getLength()),
                         Integer.min(licenseList.getLength(), workList.getLength())
                 ); i++) {
                arbiters.add(new Arbiter(
                        ((Element) nameList.item(i)).getAttribute("value"),
                        ArbiterTitle.getAribterTitle(((Element) titleList.item(i)).getAttribute("value")),
                        ((Element) licenseList.item(i)).getAttribute("value"),
                        ((Element) workList.item(i)).getAttribute("value")
                ));
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            Field[] fields = this.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true); // Make private fields accessible
                    String fieldName = field.getName();
                    Object fieldValue = field.get(this);
                    result.append(fieldName).append(": ").append(fieldValue).append("\n");
                }
            } catch (IllegalAccessException ignored) {
            }
            return result.toString();
        }

        public Arbiter getChiefArbiter() {
            return chiefArbiter;
        }

        public void setChiefArbiter(Arbiter chiefArbiter) {
            this.chiefArbiter = chiefArbiter;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getRateOfPlay() {
            return rateOfPlay;
        }

        public void setRateOfPlay(String rateOfPlay) {
            this.rateOfPlay = rateOfPlay;
        }

        public ArrayList<SwsxEvent> getSchedule() {
            return schedule;
        }

        public void setSchedule(ArrayList<SwsxEvent> schedule) {
            this.schedule = schedule;
        }

        public ArrayList<Arbiter> getArbiters() {
            return arbiters;
        }

        public void setArbiters(ArrayList<Arbiter> arbiters) {
            this.arbiters = arbiters;
        }

    }

    public class TournamentReportFide {
        private String name;
        private String place;
        private Federation federation;
        private Arbiter chiefArbiter;
        private String rateOfPlay;

        TournamentReportFide(Element report) {
            Element nameNode = (Element) report.getElementsByTagName("tournament_name").item(0);
            Element placeNode = (Element) report.getElementsByTagName("tournament_place").item(0);
            Element fedNode = (Element) report.getElementsByTagName("tournament_fed").item(0);
            Element chiefNode = (Element) report.getElementsByTagName("chief_arbiter").item(0);
            Element rateNode = (Element) report.getElementsByTagName("rate_of_play").item(0);

            setName(nameNode.getAttribute("value"));
            setPlace(placeNode.getAttribute("value"));
            try {
                setFederation(Federation.valueOf(fedNode.getAttribute("value")));
            } catch (IllegalArgumentException e) {
                setFederation(Federation.FID);
            }
            setChiefArbiter(new Arbiter(chiefNode.getAttribute("value")));
            setRateOfPlay(rateNode.getAttribute("value"));

        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            Field[] fields = this.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true); // Make private fields accessible
                    String fieldName = field.getName();
                    Object fieldValue = field.get(this);
                    result.append(fieldName).append(": ").append(fieldValue).append("\n");
                }
            } catch (IllegalAccessException e) {
            }
            return result.toString();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public Federation getFederation() {
            return federation;
        }

        public void setFederation(Federation federation) {
            this.federation = federation;
        }

        public Arbiter getChiefArbiter() {
            return chiefArbiter;
        }

        public void setChiefArbiter(Arbiter chiefArbiter) {
            this.chiefArbiter = chiefArbiter;
        }

        public String getRateOfPlay() {
            return rateOfPlay;
        }

        public void setRateOfPlay(String rateOfPlay) {
            this.rateOfPlay = rateOfPlay;
        }
    }

    public class SwsxEvent {
        Date date;
        String name;
        EventType type;

        SwsxEvent(Element element) {
            Element nameNode = (Element) element.getElementsByTagName("event_name").item(0);
            Element dateNode = (Element) element.getElementsByTagName("event_time").item(0);
            Element typeNode = (Element) element.getElementsByTagName("type").item(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            setName(nameNode.getAttribute("value"));
            switch (typeNode.getAttribute("value")) {
                case "0" -> setType(EventType.BRIEFING);
                case "2" -> setType(EventType.ENDING);
                default -> setType(EventType.ROUND);
            }

            try {
                setDate(
                        dateFormat.parse(dateNode.getAttribute("year" + "/" +
                                dateNode.getAttribute("month") + "/" +
                                dateNode.getAttribute("day")))
                );
            } catch (ParseException e) {
                setDate(Date.from(LocalDate.of(1899, 12, 31).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }

        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public EventType getType() {
            return type;
        }

        public void setType(EventType type) {
            this.type = type;
        }


        enum EventType {
            BRIEFING,
            ROUND,
            ENDING
        }
    }

    public class SwsxPlayer {
        boolean forfeitFromTournament;
        Player.Sex sex;
        Float manualTiebreak;
        byte dayOfBorn;
        byte monthOfBorn;
        short yearOfBorn;
        UUID playerId;
        short fideRatingClassic;
        short fideRatingRapid;
        short fideRatingBlitz;
        short localRating;
        int fideId;
        int polId;
        Federation federation;
        Title title;
        String club;
        String license;
        String fullName;
        String state;
        ArrayList<SwsxRound> rounds = new ArrayList<>();

        SwsxPlayer(Element cobarrayItem) throws XPathExpressionException {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression roundsExpression = xPath.compile(".//rounds/cobarray_item");

            Element nameSurnameNode = (Element) cobarrayItem.getElementsByTagName("name_surname").item(0);
            Element codeFideNode = (Element) cobarrayItem.getElementsByTagName("code_fide").item(0);
            Element dateBornYearNode = (Element) cobarrayItem.getElementsByTagName("date_of_born_year").item(0);
            Element dateBornMonthNode = (Element) cobarrayItem.getElementsByTagName("date_of_born_month").item(0);
            Element dateBornDayNode = (Element) cobarrayItem.getElementsByTagName("date_of_born_day").item(0);
            Element ratingFideNode = (Element) cobarrayItem.getElementsByTagName("rating_fide").item(0);
            Element ratingFideRNode = (Element) cobarrayItem.getElementsByTagName("rating_fide_r").item(0);
            Element ratingFideBNode = (Element) cobarrayItem.getElementsByTagName("rating_fide_b").item(0);
            Element localRatingNode = (Element) cobarrayItem.getElementsByTagName("local_rating").item(0);
            Element sexNode = (Element) cobarrayItem.getElementsByTagName("sex").item(0);
            Element idcrNode = (Element) cobarrayItem.getElementsByTagName("idcr").item(0);
            Element titleNode = (Element) cobarrayItem.getElementsByTagName("title").item(0);
            Element fedNode = (Element) cobarrayItem.getElementsByTagName("fed").item(0);
            Element licenceNode = (Element) cobarrayItem.getElementsByTagName("licence").item(0);
            Element clubNode = (Element) cobarrayItem.getElementsByTagName("club").item(0);
            Element playerIdNode = (Element) cobarrayItem.getElementsByTagName("player_id").item(0);

            NodeList roundsNode = (NodeList) roundsExpression.evaluate(cobarrayItem, XPathConstants.NODESET);

            String nameSurname = nameSurnameNode.getAttribute("value");
            String codeFide = codeFideNode.getAttribute("value");
            String dateBornYear = dateBornYearNode.getAttribute("value");
            String dateBornMonth = dateBornMonthNode.getAttribute("value");
            String dateBornDay = dateBornDayNode.getAttribute("value");
            String ratingFide = ratingFideNode.getAttribute("value");
            String ratingFideR = ratingFideRNode.getAttribute("value");
            String ratingFideB = ratingFideBNode.getAttribute("value");
            String localRating = localRatingNode.getAttribute("value");
            String sex = sexNode.getAttribute("value");
            String idcr = idcrNode.getAttribute("value");
            String title = titleNode.getAttribute("value");
            String fed = fedNode.getAttribute("value");
            String licence = licenceNode.getAttribute("value");
            String club = clubNode.getAttribute("value");
            String playerId = playerIdNode.getAttribute("value");


            setFullName(nameSurname);
            try {
                setFideId(Integer.parseInt(codeFide));
            } catch (NumberFormatException e) {
                setFideId(0);
            }
            setYearOfBorn(Short.parseShort(dateBornYear));
            setMonthOfBorn(Byte.parseByte(dateBornMonth));
            setDayOfBorn(Byte.parseByte(dateBornDay));
            setFideRatingClassic(Short.parseShort(ratingFide));
            setFideRatingRapid(Short.parseShort(ratingFideR));
            setFideRatingBlitz(Short.parseShort(ratingFideB));
            setLocalRating(Short.parseShort(localRating));
            setSex(sex.equals("0") ? Player.Sex.FEMALE : Player.Sex.MALE);
            try {
                setPolId(Integer.parseInt(idcr.replaceAll("PL-", "")));
            } catch (NumberFormatException e) {
                setPolId(0);
            }
            setTitle(Title.getTitle(title));
            try {
                setFederation(Federation.valueOf(fed));
            } catch (IllegalArgumentException e) {
                setFederation(Federation.FID);
            }
            setLicense(licence);
            setClub(club);

            long longNumber = Integer.parseInt(playerId) & 0xFFFFFFFFL;
            UUID uuid = new UUID(0, longNumber);
            setPlayerId(uuid);


            for (int j = 0; j < roundsNode.getLength(); j++) {
                Element round = (Element) roundsNode.item(j);

                Element color_piecesNode = (Element) round.getElementsByTagName("color_pieces").item(0);
                Element player_statusNode = (Element) round.getElementsByTagName("player_status").item(0);
                Element result_symbolNode = (Element) round.getElementsByTagName("result_symbol").item(0);
                Element resultNode = (Element) round.getElementsByTagName("result").item(0);
                Element opponent_idNode = (Element) round.getElementsByTagName("opponent_id").item(0);
                Element pair_noNode = (Element) round.getElementsByTagName("pair_no").item(0);

                String colorPieces = color_piecesNode.getAttribute("value");
                String playerStatus = player_statusNode.getAttribute("value");
                String resultSymbol = result_symbolNode.getAttribute("value");
                String resultPoints = resultNode.getAttribute("value");
                String opponentId = opponent_idNode.getAttribute("value");
                String pairNo = pair_noNode.getAttribute("value");

                Player.Color color;
                switch (colorPieces) {
                    case "1" -> color = Player.Color.WHITE;
                    case "2" -> color = Player.Color.BLACK;
                    default -> color = null;
                }
                Result result;
                switch (resultSymbol) {
                    case "1" -> result = Result.WIN;
                    case "2" -> result = Result.LOSE;
                    case "3" -> result = Result.DRAW;
                    default -> result = null;
                }


                rounds.add(new SwsxRound(color, Byte.parseByte(playerStatus), result, Float.parseFloat(resultPoints.replaceAll(",", ".")), Short.parseShort(opponentId), Short.parseShort(pairNo)));
            }
        }

        @Override
        public String toString() {
            return
                    "\n===============================\n" +
                            "Name/Surname: " + getFullName() + "\n" +
                            "Code FIDE: " + getFideId() + "\n" +
                            "Date of Birth (Year): " + getYearOfBorn() + "\n" +
                            "Date of Birth (Month): " + getMonthOfBorn() + "\n" +
                            "Date of Birth (Day): " + getDayOfBorn() + "\n" +
                            "Rating FIDE: " + getFideRatingClassic() + "\n" +
                            "Rating FIDE R: " + getFideRatingRapid() + "\n" +
                            "Rating FIDE B: " + getFideRatingBlitz() + "\n" +
                            "Local Rating: " + getLocalRating() + "\n" +
                            "Sex: " + getSex() + "\n" +
                            "IDCR: " + getPolId() + "\n" +
                            "Title: " + getTitle() + "\n" +
                            "FED: " + getFederation() + "\n" +
                            "Licence: " + getLicense() + "\n" +
                            "Club: " + getClub() + "\n" +
                            "rounds: " + getRounds() + "\n" +
                            "===============================";

        }

        public boolean isForfeitFromTournament() {
            return forfeitFromTournament;
        }

        public void setForfeitFromTournament(boolean forfeitFromTournament) {
            this.forfeitFromTournament = forfeitFromTournament;
        }

        public Player.Sex getSex() {
            return sex;
        }

        public void setSex(Player.Sex sex) {
            this.sex = sex;
        }

        public Float getManualTiebreak() {
            return manualTiebreak;
        }

        public void setManualTiebreak(Float manualTiebreak) {
            this.manualTiebreak = manualTiebreak;
        }

        public byte getDayOfBorn() {
            return dayOfBorn;
        }

        public void setDayOfBorn(byte dayOfBorn) {
            this.dayOfBorn = dayOfBorn;
        }

        public byte getMonthOfBorn() {
            return monthOfBorn;
        }

        public void setMonthOfBorn(byte monthOfBorn) {
            this.monthOfBorn = monthOfBorn;
        }

        public short getYearOfBorn() {
            return yearOfBorn;
        }

        public void setYearOfBorn(short yearOfBorn) {
            this.yearOfBorn = yearOfBorn;
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public void setPlayerId(UUID playerId) {
            this.playerId = playerId;
        }

        public short getFideRatingClassic() {
            return fideRatingClassic;
        }

        public void setFideRatingClassic(short fideRatingClassic) {
            this.fideRatingClassic = fideRatingClassic;
        }

        public short getFideRatingRapid() {
            return fideRatingRapid;
        }

        public void setFideRatingRapid(short fideRatingRapid) {
            this.fideRatingRapid = fideRatingRapid;
        }

        public short getFideRatingBlitz() {
            return fideRatingBlitz;
        }

        public void setFideRatingBlitz(short fideRatingBlitz) {
            this.fideRatingBlitz = fideRatingBlitz;
        }

        public short getLocalRating() {
            return localRating;
        }

        public void setLocalRating(short localRating) {
            this.localRating = localRating;
        }

        public int getFideId() {
            return fideId;
        }

        public void setFideId(int fideId) {
            this.fideId = fideId;
        }

        public int getPolId() {
            return polId;
        }

        public void setPolId(int polId) {
            this.polId = polId;
        }

        public Federation getFederation() {
            return federation;
        }

        public void setFederation(Federation federation) {
            this.federation = federation;
        }

        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public String getClub() {
            return club;
        }

        public void setClub(String club) {
            this.club = club;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public ArrayList<SwsxRound> getRounds() {
            return rounds;
        }

        public void setRounds(ArrayList<SwsxRound> rounds) {
            this.rounds = rounds;
        }


    }

    public class SwsxRound {
        Player.Color color;
        byte status;
        Result result;
        Float points;
        short opponentId;
        short pairNo;

        public SwsxRound(Player.Color color, byte status, Result result, Float points, short opponentId, short pairNo) {
            setColor(color);
            setStatus(status);
            setResult(result);
            setPoints(points);
            setOpponentId(opponentId);
            setPairNo(pairNo);
        }

        @Override
        public String toString() {
            return color + " " + status + " " + result + " " + opponentId + " " + pairNo;
        }


        public Player.Color getColor() {
            return color;
        }

        public void setColor(Player.Color color) {
            this.color = color;
        }

        public byte getStatus() {
            return status;
        }

        public void setStatus(byte status) {
            this.status = status;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public Float getPoints() {
            return points;
        }

        public void setPoints(Float points) {
            this.points = points;
        }

        public short getOpponentId() {
            return opponentId;
        }

        public void setOpponentId(short opponentId) {
            this.opponentId = opponentId;
        }

        public short getPairNo() {
            return pairNo;
        }

        public void setPairNo(short pairNo) {
            this.pairNo = pairNo;
        }

    }

}
