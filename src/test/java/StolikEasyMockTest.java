import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.easymock.EasyMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class StolikEasyMockTest {
    private IStolik stolikMock;
    private StolikMetody stolikMetody;

    @BeforeEach
    public void initEach() {
        stolikMock = EasyMock.createMock(IStolik.class);
        stolikMetody = new StolikMetody(stolikMock);
    }

    @Test
    public void getAllStolik_empty_list() {
        expect(stolikMock.getAll()).andReturn(new ArrayList<Stolik>());
        replay(stolikMock);
        assertThat(stolikMetody.getAllStolik()).isEqualTo(new ArrayList<Stolik>());
    }
    @Test
    public void getAllStolik_returnList() {
        Stolik stolik1 = new Stolik(1, 3);
        Stolik stolik2 = new Stolik(2, 3);
        Stolik stolik3 = new Stolik(3, 3);
        Stolik stolik4 = new Stolik(4, 3);

        List<Stolik> expected = new ArrayList<Stolik>();
        expected.add(stolik1);
        expected.add(stolik2);
        expected.add(stolik3);
        expected.add(stolik4);
        expect(stolikMock.getAll()).andReturn(expected);
        replay(stolikMock);
        assertThat(stolikMetody.getAllStolik()).isEqualTo(expected);
    }
    @Test
    public void getStolikbyNumer_Null() {
        expect(stolikMock.getByNumer(1)).andReturn(null);
        replay(stolikMock);
        assertThat(stolikMetody.getStolikByNumer(1)).isNull();
    }
    @Test
    public void getStolikbyNumer_ok() {
        Stolik stolik = new Stolik(1, 3);
        expect(stolikMock.getByNumer(1)).andReturn(stolik);
        replay(stolikMock);
        assertThat(stolikMetody.getStolikByNumer(1)).isSameAs(stolik);
    }

    @Test
    public void addStolik_True() {
        Stolik stolik = new Stolik(1, 3);
        expect(stolikMock.add(stolik)).andReturn(true);
        replay(stolikMock);
        assertThat(stolikMetody.addStolik(stolik)).isTrue();
    }

    @Test
    public void updateNullStolikException() {
        assertThatThrownBy(() -> stolikMetody.updateStolik(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateStolik_False() {
        Stolik stolik = new Stolik(1, 3);
        expect(stolikMock.update(stolik)).andReturn(false);
        replay(stolikMock);
        assertThat(stolikMetody.updateStolik(stolik)).isFalse();
    }

    @Test
    public void updateStolik_true() {
        Stolik stolik = new Stolik(1, 3);
        expect(stolikMock.update(stolik)).andReturn(true);
        replay(stolikMock);
        assertThat(stolikMetody.updateStolik(stolik)).isTrue();
    }

    @Test
    public void deleteStolikNullException() {
        assertThatThrownBy(() -> stolikMetody.deleteStolik(null))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
