package com.example.mentalhealthsystem.constants;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;@Slf4j
@UtilityClass
public class AssessmentInfo {
    private static final Map<String, Integer> NUM_QUESTIONS = new HashMap<>();


    static {
        NUM_QUESTIONS.put("anxiety", 7);
        NUM_QUESTIONS.put("depression", 9);
        NUM_QUESTIONS.put("self-esteem", 10);
        NUM_QUESTIONS.put("socialshyness", 15);
        NUM_QUESTIONS.put("obsessivecompulsive", 13);
        NUM_QUESTIONS.put("burnout", 15);
        NUM_QUESTIONS.put("narcissistic", 9);
    }
    @Getter
    private static final List<String> ANSWERS = new ArrayList<>();
    static {
        ANSWERS.add("مُطْلَقاً");
        ANSWERS.add("نادرًا");
        ANSWERS.add("أحيانا");
        ANSWERS.add("غالباً");
        ANSWERS.add("دائما");
    }

    @Getter
    private static final Map<String, String> ASSESSMENTSNAMES = new HashMap<>();
    static {
        ASSESSMENTSNAMES.put("Anxiety", "القلق");
        ASSESSMENTSNAMES.put("Depression", "الاكتئاب");
        ASSESSMENTSNAMES.put("Self-Esteem", "التقدير الذاتي");
        ASSESSMENTSNAMES.put("SocialShyness", "الخجل الاجتماعي");
        ASSESSMENTSNAMES.put("ObsessiveCompulsive", "الوسواس القهري");
        ASSESSMENTSNAMES.put("Burnout", "الاحتراق الوظيفي");
        ASSESSMENTSNAMES.put("Narcissistic", "الشخصية النرجسية");

    }
    public static String getAssessmentName(String key) {
        log.error("befor getAssessmentName Key: " + key + ", Name: " + ASSESSMENTSNAMES.getOrDefault(key, key));
        return ASSESSMENTSNAMES.getOrDefault(key, key);
    }

    private static final Set<String> ANXIETY_QUESTIONS = new HashSet<>();
    static {
        ANXIETY_QUESTIONS.add("الشعور بالتوتر أو القلق أو التوتر");
        ANXIETY_QUESTIONS.add("عدم القدرة على التوقف أو السيطرة على القلق");
        ANXIETY_QUESTIONS.add("القلق الشديد بشأن أشياء مختلفة");
        ANXIETY_QUESTIONS.add("صعوبة في الاسترخاء");
        ANXIETY_QUESTIONS.add("الشعور بالقلق الشديد بحيث يصعب الجلوس ساكنًا");
        ANXIETY_QUESTIONS.add("سهولة الانزعاج أو الانفعال");
        ANXIETY_QUESTIONS.add("الشعور بالخوف، وكأن شيئاً فظيعاً قد يحدث");

    }
    private static final Set<String> BURNOUT_QUESTIONS = new HashSet<>();
    static {
        BURNOUT_QUESTIONS.add("أشعر بالإرهاق واستنزاف الطاقة الجسدية أو العاطفية.");
        BURNOUT_QUESTIONS.add("لدي أفكار سلبية حول وظيفتي.");
        BURNOUT_QUESTIONS.add("أنا أصعب وأقل تعاطفاً مع الناس مما يستحقونه.");
        BURNOUT_QUESTIONS.add("أغضب بسهولة بسبب المشاكل الصغيرة، أو بسبب زملائي في العمل وفريق العمل.");
        BURNOUT_QUESTIONS.add("أشعر بسوء الفهم أو عدم التقدير من قبل زملائي في العمل.");
        BURNOUT_QUESTIONS.add("أشعر أنه ليس لدي من أتحدث معه.");
        BURNOUT_QUESTIONS.add(" أشعر بأنني أحقق أقل مما ينبغي.");
        BURNOUT_QUESTIONS.add("أشعر بأنني تحت مستوى غير مريح من الضغط لتحقيق النجاح.");
        BURNOUT_QUESTIONS.add("أشعر أنني لا أحصل على ما أريد من وظيفتي");
        BURNOUT_QUESTIONS.add("أشعر أنني في المنظمة الخطأ أو المهنة الخطأ.");
        BURNOUT_QUESTIONS.add("أشعر بالإحباط بسبب أجزاء من وظيفتي.");
        BURNOUT_QUESTIONS.add("أشعر أن السياسات التنظيمية أو البيروقراطية تحبط قدرتي على القيام بعمل جيد.");
        BURNOUT_QUESTIONS.add("أشعر أن هناك المزيد من العمل الذي يتعين علي القيام به أكثر مما لدي القدرة على القيام به عمليًا.");
        BURNOUT_QUESTIONS.add("أشعر أنه ليس لدي الوقت للقيام بالعديد من الأشياء المهمة للقيام بعمل جيد.");
        BURNOUT_QUESTIONS.add("أجد أنه ليس لدي الوقت للتخطيط بقدر ما أريد.");

    }
    private static final Set<String> DEPRESSION_QUESTIONS = new HashSet<>();
    static {
        DEPRESSION_QUESTIONS.add("قلة الاهتمام أو المتعة في القيام بالأشياء.");
        DEPRESSION_QUESTIONS.add("الشعور بالإحباط أو الاكتئاب أو اليأس.");
        DEPRESSION_QUESTIONS.add("صعوبة في النوم أو الاستمرار فيه، أو النوم أكثر من اللازم.");
        DEPRESSION_QUESTIONS.add("الشعور بالتعب أو قلة الطاقة.");
        DEPRESSION_QUESTIONS.add("ضعف الشهية أو الإفراط في تناول الطعام.");
        DEPRESSION_QUESTIONS.add("الشعور بالسوء تجاه نفسك - أو أنك فاشل أو خذلت نفسك أو عائلتك.");
        DEPRESSION_QUESTIONS.add("صعوبة في التركيز على الأشياء، مثل قراءة الجريدة أو مشاهدة التلفاز.");
        DEPRESSION_QUESTIONS.add("التحرك أو التحدث ببطء شديد لدرجة أن الآخرين قد يلاحظون ذلك أو العكس – التململ أو القلق الشديد لدرجة أنك تتحرك أكثر من المعتاد.");
        DEPRESSION_QUESTIONS.add("الأفكار التي من الأفضل أن تموت أو أن تؤذي نفسك.");

    }
    private static final Set<String> NARCISSISTIC_QUESTIONS = new HashSet<>();
    static {
        NARCISSISTIC_QUESTIONS.add("هل تشعر بإحساس مبالغ فيه بأهمية الذات والذي يتضمن في كثير من الأحيان الحاجة إلى المبالغة في مواهبك أو إنجازاتك");
        NARCISSISTIC_QUESTIONS.add("هل تعتقد أنك مميز وفريد \u200B\u200Bمن نوعه ولا يمكن فهمه إلا من قبل أشخاص أو مؤسسات أخرى مميزة أو رفيعة المستوى أو ينبغي عليك الارتباط بها");
        NARCISSISTIC_QUESTIONS.add(" هل تجد أن لديك رغبة دائمة في استغلال الآخرين لتحقيق أهدافك الخاصة");
        NARCISSISTIC_QUESTIONS.add("هل تحتاج إلى الإعجاب الزائد من الآخرين");
        NARCISSISTIC_QUESTIONS.add("هل أنت منشغل بأوهام النجاح اللامحدود، أو القوة، أو الذكاء، أو الجمال، أو الحب المثالي");
        NARCISSISTIC_QUESTIONS.add("هل لديك شعور بالاستحقاق من الآخرين والذي يتضمن توقعات غير معقولة بمعاملة تفضيلية خاصة أو الامتثال التلقائي لتوقعاتهم");
        NARCISSISTIC_QUESTIONS.add("هل تجد أنك غير راغب في التعرف على مشاعر واحتياجات الآخرين أو التعرف عليها");
        NARCISSISTIC_QUESTIONS.add("هل ينظر إليك الآخرون على أنك متعجرف أو متعجرف");
        NARCISSISTIC_QUESTIONS.add("هل تجد أنك غالبًا ما تحسد الآخرين و/أو تعتقد أن الآخرين يغارون منك");


    }
    private static final Set<String> OBSESSIVECOMPULSIVE_QUESTIONS = new HashSet<>();
    static {
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقلق بشأن الجراثيم أو المرض أو الموت");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل لديك مخاوف شديدة من حدوث أشياء سيئة أو القيام بشيء خاطئ");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تشعر أن الأمور يجب أن تكون صحيحة تمامًا");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تراودك أفكار أو صور مزعجة وغير مرغوب فيها حول إيذاء الآخرين، ولكنك لا تريد أن تؤذي أحدا");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تراودك أفكار أو صور مزعجة وغير مرغوب فيها ذات طبيعة جنسية");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقلق كثيرًا بشأن فعل الشيء الخطأ");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تنزعج من الأفكار التي تجدها محرجة أو مقززة");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقوم بالفحص المفرط (إعادة التحقق من أن الباب مغلق وأن الفرن مطفأ");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقوم بالغسيل و/أو التنظيف بشكل مفرط");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تكرر الأفعال حتى تصبح \"صحيحة\" أم تبدأ الأمور من جديد");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقوم بطلب أو ترتيب الأشياء أو العناصر");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تحتاج إلى طمأنينة مفرطة (على سبيل المثال، السؤال الدائم: \"هل أنت متأكد من أنني سأكون بخير");
        OBSESSIVECOMPULSIVE_QUESTIONS.add("هل تقضي أكثر من 3 ساعات يومياً في محاربة الوساوس أو الأفعال القهرية");

    }
    private static final Set<String> SELF_ESTEEM_QUESTIONS = new HashSet<>();
    static {
        SELF_ESTEEM_QUESTIONS.add("هل تعتقد أنك أقل شأنا من الآخرين بطريقة ما");
        SELF_ESTEEM_QUESTIONS.add("هل تعتقد أنك شخص جدير بالاهتمام");
        SELF_ESTEEM_QUESTIONS.add("هل أنت حساس تجاه النقد");
        SELF_ESTEEM_QUESTIONS.add("هل تشعر بالفشل الشامل");
        SELF_ESTEEM_QUESTIONS.add("كم مرة تفكر بإيجابية في نفسك");
        SELF_ESTEEM_QUESTIONS.add("هل تحب نفسك وتتقبلها حتى عندما يرفضك الآخرون");
        SELF_ESTEEM_QUESTIONS.add("هل تشعر بالتوتر عندما يُطلب منك التحدث إلى مجموعة من الأشخاص لا تعرفهم");
        SELF_ESTEEM_QUESTIONS.add("هل أنت راضٍ عن الشخص الذي أصبحت عليه");
        SELF_ESTEEM_QUESTIONS.add("هل تشعر بالرضا عن نفسك دون التحقق المنتظم من الآخرين");
        SELF_ESTEEM_QUESTIONS.add("كيف تقيم مستوى احترامك لذاتك");
    }

    private static final Set<String> SOCIALSHYNESS_QUESTIONS = new HashSet<>();
    static {
        SOCIALSHYNESS_QUESTIONS.add("أشعر بالخجل في المواقف الاجتماعية");
        SOCIALSHYNESS_QUESTIONS.add("أميل إلى احمرار وجهي أو الشعور بالدفء عند وجود أشخاص غير مألوفين.");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في بدء محادثات مع الغرباء.");
        SOCIALSHYNESS_QUESTIONS.add("لا أستطيع الانفتاح في المناسبات الاجتماعية أو التجمعات.");
        SOCIALSHYNESS_QUESTIONS.add("أشعر بوعي مفرط عندما أكون مركز الاهتمام.");
        SOCIALSHYNESS_QUESTIONS.add("أفضّل التزام الصمت ومراقبة الآخرين في إطار المجموعة.");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في التواصل البصري مع الآخرين.");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في التحدث والتعبير عن آرائي، خاصة عندما أختلف مع شخص ما.");
        SOCIALSHYNESS_QUESTIONS.add("أميل إلى الإفراط في التفكير أو التحليل بعمق إذا قدم لي شخص ما أي تعليقات.");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في سؤال الناس عن أية معلومات.");
        SOCIALSHYNESS_QUESTIONS.add("أشعر بعدم الراحة عند اللمس الجسدي، مثل التحية أو المعانقة، عندما أقابل الناس");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في التحدث عن نفسي أمام الآخرين");
        SOCIALSHYNESS_QUESTIONS.add("أشعر بعدم الراحة عند التحدث مع الأشخاص من الجنس الآخر");
        SOCIALSHYNESS_QUESTIONS.add("أفضل أن يكون لدي عدد قليل من الأصدقاء المقربين بدلاً من دائرة اجتماعية أكبر");
        SOCIALSHYNESS_QUESTIONS.add("أجد صعوبة في الدخول في علاقات وثيقة وحميمة مع شخص آخر");

    }
    private static final Map<String, Set<String>> ASSESSMENT_QUESTIONS = new HashMap<>();

    static {
        ASSESSMENT_QUESTIONS.put("anxiety", ANXIETY_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("burnout", BURNOUT_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("self-esteem", SELF_ESTEEM_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("socialshyness", SOCIALSHYNESS_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("obsessivecompulsive", OBSESSIVECOMPULSIVE_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("depression", DEPRESSION_QUESTIONS);
        ASSESSMENT_QUESTIONS.put("narcissistic", NARCISSISTIC_QUESTIONS);
    }

    public static int getNumberOfQuestions(String assessmentName) {
        assessmentName = assessmentName.toLowerCase(Locale.ROOT);
        if (NUM_QUESTIONS.containsKey(assessmentName)) {
            return NUM_QUESTIONS.get(assessmentName);
        } else {
            throw new IllegalArgumentException("Assessment name not recognized: " + assessmentName);
        }
    }



    public static Set<String> getAssessmentQuestions(String assessmentName) {
        assessmentName = assessmentName.toLowerCase(Locale.ROOT);
        if (ASSESSMENT_QUESTIONS.containsKey(assessmentName)) {
            return ASSESSMENT_QUESTIONS.get(assessmentName);
        } else {
            throw new IllegalArgumentException("Assessment name not recognized: " + assessmentName);
        }
    }
}

